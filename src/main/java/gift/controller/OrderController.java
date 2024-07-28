package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.order.OrderRequestDTO;
import gift.dto.betweenClient.order.OrderResponseDTO;
import gift.service.KakaoTokenService;
import gift.service.MemberService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishListService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private final KakaoTokenService kakaoTokenService;
    private final MemberService memberService;
    private final OptionService optionService;
    private final WishListService wishListService;
    private final OrderService orderService;

    public OrderController(KakaoTokenService kakaoTokenService, MemberService memberService,
            OptionService optionService, WishListService wishListService,
            OrderService orderService) {
        this.kakaoTokenService = kakaoTokenService;
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishListService = wishListService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> order(@LoginMember MemberDTO memberDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        String accessToken = memberService.getMemberAccessToken(memberDTO.getEmail());

        optionService.subtractOptionQuantity(orderRequestDTO.optionId(),
                orderRequestDTO.quantity());

        Map<String, String> orderInfo = optionService.getOptionInfoForOrder(
                orderRequestDTO.optionId());

        wishListService.removeWishListProduct(memberDTO, Long.valueOf(orderInfo.get("productId")));
        kakaoTokenService.sendMsgToMe(accessToken, orderInfo, orderRequestDTO.message());
        OrderResponseDTO orderResponseDTO = orderService.saveOrderHistory(orderRequestDTO);

        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }
}
