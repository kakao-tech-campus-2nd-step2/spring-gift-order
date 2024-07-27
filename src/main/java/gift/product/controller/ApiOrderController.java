package gift.product.controller;

import static gift.product.exception.GlobalExceptionHandler.UNKNOWN_VALIDATION_ERROR;

import gift.product.dto.OrderDTO;
import gift.product.model.Order;
import gift.product.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class ApiOrderController {

    private final OrderService orderService;

    @Autowired
    public ApiOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{optionId}")
    public ResponseEntity<Order> orderProduct(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long optionId,
        @Valid @RequestBody OrderDTO orderDTO,
        BindingResult bindingResult) {
        System.out.println("[ApiOrderController] orderProduct()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.orderProduct(authorization, optionId, orderDTO));
    }
}
