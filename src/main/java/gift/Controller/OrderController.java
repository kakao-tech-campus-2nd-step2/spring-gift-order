package gift.Controller;

import gift.DTO.OrderDto;
import gift.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private OrderService orderService;

  public OrderController(OrderService orderService){
    this.orderService=orderService;
  }

  //kakaoLoginUser 하기
  @PostMapping
  public ResponseEntity<String> orderOption(@RequestBody OrderDto orderDto) throws IllegalAccessException {
    String response = orderService.orderOption(orderDto);
    return ResponseEntity.ok(response);
  }

}
