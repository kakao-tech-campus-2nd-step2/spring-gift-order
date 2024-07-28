package gift.Controller;

import gift.DTO.RequestOrderDTO;
import gift.DTO.ResponseOrderDTO;
import gift.Model.Entity.Member;
import gift.Service.OrderService;
import gift.annotation.ValidUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseOrderDTO>> getOrders(@ValidUser Member member){
        List<ResponseOrderDTO> orders = orderService.getOrders(member);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseOrderDTO> createOrder(@ValidUser Member member, @Valid @RequestBody RequestOrderDTO requestOrderDTO){
        ResponseOrderDTO response =orderService.createOrder(member, requestOrderDTO);
        return ResponseEntity.created(URI.create("api/orders/"+response.getId())).body(response);
    }

    @PutMapping()
    public ResponseEntity<String> editOrder(@ValidUser Member member,
                                            @RequestParam("order-id") Long orderId,
                                            @RequestParam("edit-type") String editType,
                                            @RequestParam("delta-quantity") int delataQuantity){
        orderService.editOrder(member, orderId, editType, delataQuantity);
        return ResponseEntity.ok("주문이 정상적으로 수정되었습니다");
    }

    @DeleteMapping()
    public ResponseEntity<String > deleteOrder(@ValidUser Member member, @RequestParam("order-id") Long orderId){
        orderService.deleteOrder(member, orderId);
        return ResponseEntity.ok("주문이 정상적으로 취소되었습니다");
    }
 }
