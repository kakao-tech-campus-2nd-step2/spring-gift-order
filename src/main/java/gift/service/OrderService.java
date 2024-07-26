package gift.service;

import gift.dto.OrderRequestDto;
import gift.repository.OrderRepository;
import gift.vo.Member;
import gift.vo.Option;
import gift.vo.Order;
import gift.vo.Product;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final MemberService memberService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishlistService wishlistService, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.memberService = memberService;
    }

    private void removeWish(Long wishId) {
        wishlistService.deleteWishProduct(wishId);
    }

    private void checkWishAndRemove(Long memberId, Long optionId) {
        Member member = memberService.getMemberById(memberId);
        Option option = optionService.getOption(optionId);
        Product product = option.getProduct();
        Long foundWishId = wishlistService.hasFindWishByMemberAndProduct(member, product);
        if (foundWishId != null) {
            removeWish(foundWishId);
        }
    }

    public Order createOrder(Long memberId, OrderRequestDto orderRequestDto) {
        optionService.subtractOptionQuantity(orderRequestDto.optionId(), orderRequestDto.quantity());
        Order savedOrder = orderRepository.save(orderRequestDto.toOrder(memberId));
        checkWishAndRemove(memberId, savedOrder.getOptionId());
        return savedOrder;
    }

}
