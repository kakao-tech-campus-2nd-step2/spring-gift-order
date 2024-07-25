package gift.service;

import gift.common.enums.SocialLoginType;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.OrderRequest;
import gift.controller.dto.response.OrderResponse;
import gift.model.Member;
import gift.model.Option;
import gift.model.Orders;
import gift.model.Product;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private final KakaoTokenService kakaoTokenService;
    private final KakaoApiCaller kakaoApiCaller;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, MemberRepository memberRepository, KakaoTokenService kakaoTokenService, KakaoApiCaller kakaoApiCaller) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.kakaoTokenService = kakaoTokenService;
        this.kakaoApiCaller = kakaoApiCaller;
    }

    public OrderResponse createOrder(Long memberId, OrderRequest orderRequest) {
        Product product = productRepository.findProductByOptionId(orderRequest.optionId())
                        .orElseThrow(()->new EntityNotFoundException("Product with option_id " + orderRequest.optionId() + " not found"));
        Option option = product.findOptionByOptionId(orderRequest.optionId());
        Member member = memberRepository.getReferenceById(memberId);
        Orders orders = orderRepository.save(new Orders(product.getId(), option.getId(), memberId, product.getName(), option.getName(), product.getPrice(), orderRequest.quantity(), orderRequest.message()));

        sendKakaoMessage(memberId, member.getLoginType(), orders);
        // wish에 해당 상품이 존재하면 삭제
        return new OrderResponse(null, null, 0, null, null);
    }

    private void sendKakaoMessage(Long memberId, SocialLoginType type, Orders orders) {
        if(type != SocialLoginType.KAKAO) {
            return;
        }
        String accessToken = kakaoTokenService.refreshIfAccessTokenExpired(memberId);
        kakaoApiCaller.sendKakaoMessage(accessToken, orders);
    }

}
