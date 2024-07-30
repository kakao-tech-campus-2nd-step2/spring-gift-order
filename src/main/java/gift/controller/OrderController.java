package gift.controller;

import gift.dto.OrderDTO;
import gift.model.User;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

import java.util.logging.Logger;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger LOGGER = Logger.getLogger(OrderController.class.getName());

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            LOGGER.warning("User is null in session");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        LOGGER.info("User from session: " + user.getEmail());
        OrderDTO createdOrder = orderService.createOrder(orderDTO, user);
        return ResponseEntity.status(201).body(createdOrder);
    }


    @GetMapping("/order")
    public String showOrderPage() {
        return "order";  // templates/order.html 템플릿을 반환
    }
}