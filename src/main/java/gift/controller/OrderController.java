package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.JwtUtil;
import gift.service.KakaoApiService;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final KakaoApiService kakaoApiService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, KakaoApiService kakaoApiService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.kakaoApiService = kakaoApiService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping()
    public ResponseEntity<OrderResponseDto> processOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader("Authorization") String authorizationHeader) {
        Long memberId = jwtUtil.getMemberIdFromAuthorizationHeader(authorizationHeader);

        OrderResponseDto orderResponseDto = orderService.createOrder(memberId, orderRequestDto);

        String accessToken = jwtUtil.getBearerTokenFromAuthorizationHeader(authorizationHeader);

        if (! jwtUtil.isJwtToken(accessToken)) {
            kakaoApiService.sendKakaoMessage(accessToken, orderResponseDto);
        }

        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

}
