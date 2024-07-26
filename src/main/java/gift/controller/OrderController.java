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
import gift.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestHeader("Authorization") String token, @RequestBody OrderRequest orderRequest){
        
        OrderResponse orderResponse = orderService.orderOption(orderRequest);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
