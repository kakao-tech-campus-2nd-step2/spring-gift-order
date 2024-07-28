package gift.service;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Order;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OptionService optionService;
    private WishService wishService;
    private MemberService memberService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, MemberService memberService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.memberService = memberService;
    }

    public OrderResponseDto placeOrderWithMessage(OrderRequestDto orderRequestDto, Long orderMemberId) throws IllegalAccessException {

        var option = optionService.getOptionById(orderRequestDto.getOptionId());
        var member = memberService.getById(orderMemberId);

        OrderResponseDto orderResponseDto = OrderResponseDto.fromEntity(orderRepository.save(new Order(option, orderRequestDto.getQuantity(), LocalDateTime.now(), orderRequestDto.getMessage(), member)));

        optionService.subtract(option, orderResponseDto.getQuantity());
        var wish = wishService.findByProductIdAndMemberId(option.getProduct().getId(), orderMemberId);

        if (wish != null) {
            wishService.delete(wish.getId());
        }

        return orderResponseDto;
    }

}
