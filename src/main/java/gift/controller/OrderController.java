//package gift.controller;
//
//import gift.dto.ApiResponse;
//import gift.model.Order;
//import gift.model.Option;
//import gift.service.OrderService;
//import gift.repository.OptionRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    private final OrderService orderService;
//    private final OptionRepository optionRepository;
//    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
//
//    public OrderController(OrderService orderService, OptionRepository optionRepository) {
//        this.orderService = orderService;
//        this.optionRepository = optionRepository;
//    }
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestHeader("Authorization") String kakaoToken, @RequestBody Order order) {
//        try {
//            // 로그로 Order 객체의 내용을 출력
//            logger.info("Received order: {}", order);
//
//            // Option 객체 설정
//            Option option = optionRepository.findById(order.getOptionId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));
//            order.setOption(option);
//
//            Order createdOrder = orderService.createOrder(order, kakaoToken);
//            return ResponseEntity.status(201).body(new ApiResponse<>(true, "Order created successfully", createdOrder, null));
//        } catch (Exception e) {
//            e.printStackTrace(); // 예외를 로그에 출력
//            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to create order", null, e.getMessage()));
//        }
//    }
//}

package gift.controller;

import gift.service.KakaoMessageService;
import gift.service.impl.KakaoMessageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final KakaoMessageService kakaoMessageService;

    public OrderController() {
        this.kakaoMessageService = new KakaoMessageServiceImpl();
    }

    @PostMapping("/sendMessage")
    public Mono<ResponseEntity<String>> sendMessage(@RequestHeader("Authorization") String kakaoToken, @RequestBody Map<String, String> requestBody) {
        String message = requestBody.get("message");
        return kakaoMessageService.sendMessage(kakaoToken, message);
    }
}