package gift.controller;

import gift.dto.request.OrderRequest;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OptionService optionService;

    public OrderController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public void addOption(@Valid @RequestBody OrderRequest orderRequest) {
        optionService.order(orderRequest);
    }
}