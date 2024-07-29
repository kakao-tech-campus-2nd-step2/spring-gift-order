package gift.order.service;

import gift.client.kakao.KakaoProperties;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.order.dto.KakaoMessageRequestBody;
import gift.order.dto.TemplateArgs;
import gift.order.dto.request.OrderRequest;
import gift.order.dto.response.OrderResponse;
import gift.order.entity.Order;
import gift.order.repository.OrderJpaRepository;
import gift.product.entity.Product;
import gift.product.option.entity.Option;
import gift.product.option.repository.OptionJpaRepository;
import gift.product.option.service.OptionService;
import gift.client.kakao.KakaoApiClient;
import gift.user.entity.User;
import gift.user.repository.UserJpaRepository;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    private final OptionService optionService;
    private final OptionJpaRepository optionRepository;
    private final OrderJpaRepository orderRepository;
    private final WishRepository wishRepository;
    private final KakaoProperties kakaoProperties;
    private final KakaoApiClient kakaoApiClient;
    private final UserJpaRepository userJpaRepository;


    public OrderService(OptionService optionService, OptionJpaRepository optionRepository,
        OrderJpaRepository orderRepository, WishRepository wishRepository,
        KakaoProperties kakaoProperties, KakaoApiClient kakaoApiClient,
        UserJpaRepository userJpaRepository) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiClient = kakaoApiClient;
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        optionService.subtractOptionQuantity(request.optionId(), request.quantity());
        Option option = optionRepository.findById(request.optionId())
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        User user = userJpaRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product optionProduct = option.getProduct();

        wishRepository.findByUserId(userId)
            .stream()
            .filter(wish -> wish.getProduct().equals(optionProduct))
            .forEach(wishRepository::delete);

        var templateArgs = new TemplateArgs(request.message(), optionProduct.getImageUrl(),
            optionProduct.getName());
        var kakaoMessageRequest = new KakaoMessageRequestBody(kakaoProperties.templateId(),
            templateArgs);

        Order order = new Order(request.optionId(), request.quantity(), request.message());

        Order saved = orderRepository.save(order);

        System.out.println(user.getAccessToken());

        kakaoApiClient.sendMessage(user.getAccessToken(), kakaoMessageRequest);

        return OrderResponse.from(saved);
    }
}
