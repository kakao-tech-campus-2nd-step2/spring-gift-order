package gift.controller.rest;

import gift.entity.Order;
import gift.entity.OrderDTO;
import gift.service.OrderService;
import gift.util.ResponseUtility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ResponseUtility responseUtility;

    @Autowired
    public OrderController(OrderService orderService, ResponseUtility responseUtility) {
        this.orderService = orderService;
        this.responseUtility = responseUtility;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<Order> orders = orderService.findAll(email);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Order order = orderService.save(email, orderDTO);
        // social account이면 카톡으로 전송
        String kakaoAccessToken = (String) session.getAttribute("kakaoAccessToken");
        if (kakaoAccessToken != null) {
            orderService.sendToMe(kakaoAccessToken, orderDTO);
        }
        return ResponseEntity.ok().body(order);
    }

    // 처리된 주문 제거
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        orderService.delete(id, email);
        return ResponseEntity.ok().body(responseUtility.makeResponse("Order deleted successfully"));
    }
}
