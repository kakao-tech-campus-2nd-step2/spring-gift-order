package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.exception.CustomException;
import gift.service.OrderService;
import gift.util.JwtUtil;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, JwtUtil jwtUtil){
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestHeader("Authorization") String authorizationHeader, @RequestBody OrderRequest orderRequest){
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException("Unvalid Token!", HttpStatus.BAD_REQUEST);
        }

        OrderResponse orderResponse = orderService.makeOrder(jwtUtil.extractToken(authorizationHeader), orderRequest);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
