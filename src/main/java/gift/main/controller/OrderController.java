package gift.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.main.annotation.SessionUser;
import gift.main.dto.OrderRequest;
import gift.main.dto.OrderResponce;
import gift.main.dto.UserVo;
import gift.main.service.KakaoService;
import gift.main.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;

    public OrderController(OrderService orderService,
                           KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/{id}")//@PathVariable
    public ResponseEntity<?> orderProduct(@PathVariable(name = "id") Long productId, @RequestBody OrderRequest orderRequest, @SessionUser UserVo sessionUserVo) throws JsonProcessingException {
        OrderResponce orderResponce = orderService.orderProduct(orderRequest,sessionUserVo,productId); //상품을 주문하고, 해당 상품의 옵션을 제거해야한다.
        kakaoService.SendOrderMessage(orderResponce,sessionUserVo);

        return ResponseEntity.ok(orderResponce);

    }

}
