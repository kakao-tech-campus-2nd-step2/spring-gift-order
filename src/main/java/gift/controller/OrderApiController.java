package gift.controller;

import gift.auth.CheckRole;
import gift.auth.JwtService;
import gift.exception.InputException;
import gift.request.OrderRequest;
import gift.response.OrderResponse;
import gift.service.OptionsService;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

    private final OrderService orderService;
    private final OptionsService optionsService;
    private final JwtService jwtService;

    public OrderApiController(OrderService orderService, OptionsService optionsService, JwtService jwtService) {
        this.orderService = orderService;
        this.optionsService = optionsService;
        this.jwtService = jwtService;
    }

    @CheckRole("ROLE_USER")
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> makeOrder(HttpServletRequest request, @RequestBody @Valid OrderRequest orderRequest,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }
        Long memberId = Long.valueOf(request.getAttribute("member_id").toString());
        OrderResponse dto = orderService.makeOrder(memberId, orderRequest.productId(),
            orderRequest.optionId(), orderRequest.quantity(), orderRequest.message());
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

}
