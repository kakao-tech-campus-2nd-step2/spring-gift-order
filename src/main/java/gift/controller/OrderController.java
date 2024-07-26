package gift.controller;

import static gift.util.JwtUtil.extractToken;

import gift.domain.Member;
import gift.domain.Order;
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
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<?> addOrder(HttpServletRequest request, @Valid @RequestBody Order order) {
        String token = extractToken(request);
        Claims claims = jwtUtil.extractAllClaims(token);
        Number memberId = (Number) claims.get("id");
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.addOrder((Long) memberId, order));
    }

}
