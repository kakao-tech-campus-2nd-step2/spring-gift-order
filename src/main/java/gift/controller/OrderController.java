package gift.controller;

import gift.exception.ProductNotFoundException;
import gift.model.Option;
import gift.model.Order;
import gift.service.OptionService;
import gift.service.WishlistService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class OrderController {

    @Autowired
    private final OptionService optionService;

    @Autowired
    private final WishlistService wishlistService;

    public OrderController(OptionService optionService, WishlistService wishlistService) {
        this.optionService = optionService;
        this.wishlistService = wishlistService;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token,@RequestBody Order order) {
        if (!optionService.existsOptionById(order.getOptionId())){
            throw new ProductNotFoundException("Option not found");
        }
        Option selectedOption = optionService.getOptionById(order.optionId).get();
        optionService.subtractOption(selectedOption, order.quantity);
        wishlistService.deleteById(selectedOption.getProduct().getId());
        LocalDateTime dateTime = LocalDateTime.now();
        order.setOrderDateTime(dateTime);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(order);
    }

}
