package gift.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.user.dto.UserInfo;
import gift.global.resolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order API")
public class OrderRestController {

    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "카카오 주문하기, 주문 후 메시지 전송")
    public void order(
        @Valid @RequestBody OrderRequestDTO orderRequestDTO,
        @Parameter(description = "로그인 유저 정보") @Login UserInfo userInfo
    )
        throws JsonProcessingException, UnsupportedEncodingException {
        orderService.order(orderRequestDTO, userInfo);
    }
}
