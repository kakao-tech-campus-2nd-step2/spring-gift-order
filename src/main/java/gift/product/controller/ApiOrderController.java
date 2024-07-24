package gift.product.controller;

import gift.product.dto.OrderDTO;
import gift.product.service.OrderService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<Map<String, String>> orderProduct(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {
        System.out.println("[ApiOrderController] orderProduct()");
        if(bindingResult.hasErrors())
            throw new IllegalArgumentException();
        return new ResponseEntity<>(orderService.orderProduct(orderDTO), HttpStatus.CREATED);
    }
}
