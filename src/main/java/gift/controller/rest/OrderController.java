package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Order;
import gift.entity.OrderDTO;
import gift.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<Order> orders = orderService.findAll(email);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        Order order = orderService.save(session, orderDTO);
        return ResponseEntity.ok().body(order);
    }

    // 처리된 주문 제거
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOrder(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        orderService.delete(id, email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Order deleted successfully"));
    }
}
