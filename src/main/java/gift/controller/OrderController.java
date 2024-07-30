package gift.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(HttpServletRequest request, @RequestBody OrderRequest orderRequest) {
        String email = (String) request.getAttribute("email");
        if (email == null) {
            logger.error("Email not found in request attributes");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            logger.info("Processing order for email: {}", email);
            OrderResponse orderResponse = orderService.processOrderAndSendMessage(orderRequest, email);
            logger.info("Order processed with ID: {}", orderResponse.getId());
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error processing order: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}