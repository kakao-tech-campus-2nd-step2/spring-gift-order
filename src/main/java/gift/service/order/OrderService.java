package gift.service.order;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.order.Order;
import gift.domain.order.OrderRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.domain.wish.WishRepository;
import gift.mapper.OrderMapper;
import gift.service.option.OptionService;
import gift.web.dto.MemberDto;
import gift.web.dto.OrderDto;
import gift.web.dto.Token;
import gift.web.exception.MemberNotFoundException;
import gift.web.exception.OptionNotFoundException;
import gift.web.exception.ProductNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OptionService optionService;
    private final OrderMapper orderMapper;
    private final MemberRepository memberRepository;
    private final RestClient restClient;
    private final ProductRepository productRepository;

    @Value("${kakao.send-message-template-url}")
    private String kakaoSendMessageTemplateUrl;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, OptionService optionService, OrderMapper orderMapper,
        MemberRepository memberRepository, RestClient restClient,
        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.optionService = optionService;
        this.orderMapper = orderMapper;
        this.memberRepository = memberRepository;
        this.restClient = restClient;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderDto createOrder(Token token, MemberDto memberDto, Long productId, OrderDto orderDto) {
        Option option = optionRepository.findByIdAndProductId(orderDto.optionId(), productId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        Member member = memberRepository.findByEmail(memberDto.email())
            .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .ifPresent(wishRepository::delete);

        optionService.subtractOptionQuantity(orderDto.optionId(),productId, orderDto.quantity());

        Order order = orderRepository.save(orderMapper.toEntity(orderDto, option));

        sendOrderKakaoMessage(productId, order, token);
        return orderMapper.toDto(order);
    }

    public void sendOrderKakaoMessage(Long productId, Order order, Token token) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("상품이 없습니다."));

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("product_name", product.getName());
        templateArgs.put("category_name", product.getCategory().getName());
        templateArgs.put("price", product.getPrice().toString());
        templateArgs.put("quantity", order.getQuantity().toString());

        body.add("template_id", "110540");
        body.add("template_args", templateArgs);

        restClient.post()
            .uri(URI.create(kakaoSendMessageTemplateUrl))
            .header("Authorization", "Bearer " + token.token())
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }
}
