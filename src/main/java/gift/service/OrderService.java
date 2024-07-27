package gift.service;

import gift.dto.OrderDTO;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final MemberService memberService;

    public OrderService(OrderRepository orderRepository, OptionService optionService,
        MemberService memberService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.memberService = memberService;
    }

    @Transactional
    public void createOrder(OrderDTO orderDTO, String email) {
        Option option = optionService.findOptionById(orderDTO.optionId());
        Member member = memberService.findMemberByEmail(email);
        Long quantity = orderDTO.quantity();
        optionService.subtractQuantity(option.getId(), quantity);
        Order order = new Order(null, option, quantity, LocalDateTime.now(), orderDTO.message(), member);
        orderRepository.save(order);
    }

}
