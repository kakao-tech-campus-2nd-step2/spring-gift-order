package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.request.MemberRequest;
import gift.dto.request.OrderRequest;
import gift.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> order(@RequestHeader(HttpHeaders.AUTHORIZATION) String header, @LoginMember MemberRequest memberRequest, @RequestBody OrderRequest orderRequest){
        String token = header.substring(7);
        orderService.orderOption(orderRequest, memberRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
