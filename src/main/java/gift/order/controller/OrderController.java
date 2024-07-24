package gift.order.controller;

import gift.order.dto.OrderDto;
import gift.order.entity.Order;
import gift.order.service.OrderService;
import gift.user.service.KakaoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class OrderController {

  private final OrderService orderService;
  private final KakaoApiService kakaoApiService;

  @Autowired
  public OrderController(OrderService orderService, KakaoApiService kakaoApiService) {
    this.orderService = orderService;
    this.kakaoApiService = kakaoApiService;
  }

  @PostMapping("/orders")
  public Mono<String> completeOrder(@RequestBody OrderDto orderDto, @RequestParam String userEmail) {
    return Mono.fromCallable(() -> orderService.createOrder(orderDto))
        .flatMap(order -> Mono.just("주문이 완료되었습니다."))
        .onErrorResume(e -> Mono.just("주문 처리 중 오류가 발생했습니다: " + e.getMessage()));
  }
}
