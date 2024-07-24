package gift.controller;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<OrderResponseDto> createOrder(HttpServletRequest request, @RequestBody OrderRequestDto requestDto) {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        String kakaoAccessToken = request.getHeader("Kakao-Access-Token");
        OrderResponseDto responseDto = orderService.createOrder(jwtToken, kakaoAccessToken, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
