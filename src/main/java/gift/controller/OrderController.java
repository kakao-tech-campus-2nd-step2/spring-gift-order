package gift.controller;

import static gift.util.JwtUtil.extractToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Member;
import gift.domain.Order;
import gift.service.KakaoService;
import gift.service.OrderService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, KakaoService kakaoService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> addOrder(HttpServletRequest request, @Valid @RequestBody Order order) throws JsonProcessingException {
        String token = extractToken(request);
        Claims claims = jwtUtil.extractAllClaims(token);
        Number id = (Number) claims.get("id");
        Long memberId = id.longValue();
        Order addedOrder = orderService.addOrder(memberId, order);
        kakaoService.sendOrderMessage(memberId, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedOrder);
    }

}
