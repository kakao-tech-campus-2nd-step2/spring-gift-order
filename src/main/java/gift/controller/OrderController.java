package gift.controller;

import gift.DTO.MemberDTO;
import gift.DTO.OrderDTO;
import gift.auth.LoginMember;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderDTO createOrder(@LoginMember MemberDTO memberDTO, @RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO, memberDTO);
    }
}
