package gift.order.application;

import gift.auth.application.KakaoClient;
import gift.global.error.CustomException;
import gift.member.application.MemberService;
import gift.member.entity.Member;
import gift.order.dao.OrderRepository;
import gift.order.dto.OrderRequest;
import gift.order.dto.OrderResponse;
import gift.order.util.OrderMapper;
import gift.product.application.OptionService;
import gift.product.entity.Option;
import gift.wishlist.application.WishesService;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionService optionService;
    private final WishesService wishesService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final KakaoClient kakaoClient;

    public OrderService(OptionService optionService,
                        WishesService wishesService,
                        OrderRepository orderRepository,
                        MemberService memberService,
                        KakaoClient kakaoClient) {
        this.optionService = optionService;
        this.wishesService = wishesService;
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.kakaoClient = kakaoClient;
    }

    public OrderResponse order(Long memberId,
                               OrderRequest request) {
        Option option = optionService.getOptionById(request.optionId());
        optionService.subtractQuantity(option, request.quantity());

        Long productId = option.getProductId();
        wishesService.removeWishIfPresent(memberId, productId);

        Member member = memberService.getMemberById(memberId);
        try {
            kakaoClient.sendMessageToMeOrFalse(
                    member.getKakaoAccessToken(),
                    request.message(),
                    "/member/orders/" + option.getId()
            );
        } catch (CustomException exception) {
            memberService.refreshKakaoAccessToken(memberId);
            throw exception;
        }

        return OrderMapper.toResponseDto(
                orderRepository.save(OrderMapper.toEntity(request, option, member))
        );
    }


}
