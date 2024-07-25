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
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    private final KakaoTokenService kakaoTokenService;
    private final KakaoApiCaller kakaoApiCaller;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, MemberRepository memberRepository, WishRepository wishRepository, KakaoTokenService kakaoTokenService, KakaoApiCaller kakaoApiCaller) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
        this.kakaoTokenService = kakaoTokenService;
        this.kakaoApiCaller = kakaoApiCaller;
    }

    @Transactional
    public OrderResponse createOrder(Long memberId, OrderRequest orderRequest) {
        Product product = productRepository.findProductByOptionId(orderRequest.optionId())
                        .orElseThrow(()->new EntityNotFoundException("Product with option_id " + orderRequest.optionId() + " not found"));
        Option option = product.findOptionByOptionId(orderRequest.optionId());
        Member member = memberRepository.getReferenceById(memberId);
        Orders orders = orderRepository.save(new Orders(product.getId(), option.getId(), memberId, product.getName(), option.getName(), product.getPrice(), orderRequest.quantity(), orderRequest.message()));
        subtractQuantity(orders.getProductId(), orders.getOptionId(), orders.getQuantity());
        deleteWishIfExists(product.getId(), memberId);
        sendKakaoMessage(memberId, member.getLoginType(), orders);
        return new OrderResponse(orders.getId(), orders.getOptionId(), orders.getQuantity(), orders.getCreatedAt(), orders.getDescription());
    }

    @Transactional
    public void deleteWishIfExists(Long productId, Long memberId) {
        if (wishRepository.existsByProductIdAndMemberId(productId, memberId)) {
            wishRepository.deleteByProductIdAndMemberId(productId, memberId);
        }
    }

    @Transactional
    public void subtractQuantity(Long productId, Long optionId, int amount) {
        Product product = productRepository.findProductAndOptionByIdFetchJoin(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));
        product.subtractOptionQuantity(optionId, amount);
    }

    private void sendKakaoMessage(Long memberId, SocialLoginType type, Orders orders) {
        if(type != SocialLoginType.KAKAO) {
            return;
        }
        String accessToken = kakaoTokenService.refreshIfAccessTokenExpired(memberId);
        kakaoApiCaller.sendKakaoMessage(accessToken, orders);
    }

}
