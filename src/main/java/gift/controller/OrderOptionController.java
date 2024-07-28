package gift.controller;

import gift.argumentresolver.LoginMember;
import gift.dto.MemberDTO;
import gift.dto.OrderOptionDTO;
import gift.service.OrderOptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderOptionController {

    private final OrderOptionService orderOptionService;

    @Autowired
    public OrderOptionController(OrderOptionService orderOptionService) {
        this.orderOptionService = orderOptionService;
    }

    @PostMapping
    public ResponseEntity<OrderOptionDTO> order(@LoginMember MemberDTO memberDTO, @Valid @RequestBody OrderOptionDTO orderOptionDTO) {
        return ResponseEntity.ok().body(orderOptionService.order(memberDTO, orderOptionDTO));
    }
}
