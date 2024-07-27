package gift.order.service;

import gift.member.domain.Member;
import gift.member.service.MemberService;
import gift.option.domain.Option;
import gift.option.service.OptionService;
import gift.order.OrderListResponseDto;
import gift.order.OrderResponseDto;
import gift.order.OrderServiceDto;
import gift.order.domain.Order;
import gift.order.exception.OrderNotFoundException;
import gift.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final OptionService optionService;

    public OrderService(OrderRepository orderRepository, MemberService memberService, OptionService optionService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.optionService = optionService;
    }

    public OrderListResponseDto getAllOrders() {
        return OrderListResponseDto.orderListToOptionListResponseDto(orderRepository.findAll());
    }

    public OrderResponseDto getOrderById(Long id) {
        return OrderResponseDto.orderToOrderResponseDto(orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new));
    }

    @Transactional
    public Order createOrder(OrderServiceDto orderServiceDto) {
        Member member = memberService.getMemberById(orderServiceDto.memberId());
        Option option = optionService.getOptionById(orderServiceDto.optionId());
        option.subtract(orderServiceDto.count().getOrderCountValue());
        return orderRepository.save(orderServiceDto.toOrder(member, option));
    }

    public Order updateOrder(OrderServiceDto orderServiceDto) {
        validateOrderExists(orderServiceDto.id());
        Member member = memberService.getMemberById(orderServiceDto.memberId());
        Option option = optionService.getOptionById(orderServiceDto.optionId());
        return orderRepository.save(orderServiceDto.toOrder(member, option));
    }

    public void deleteOrder(Long id) {
        validateOrderExists(id);
        orderRepository.deleteById(id);
    }

    private void validateOrderExists(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
    }
}
