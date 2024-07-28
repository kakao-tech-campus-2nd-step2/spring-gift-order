package gift.service.order;

import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.order.Order;
import gift.domain.order.OrderRepository;
import gift.domain.wish.Wish;
import gift.domain.wish.WishRepository;
import gift.mapper.OrderMapper;
import gift.service.option.OptionService;
import gift.web.dto.OrderDto;
import gift.web.exception.OptionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OptionService optionService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, OptionService optionService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.optionService = optionService;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderDto createOrder(Long memberId, Long productId, OrderDto orderDto) {
        Option option = optionRepository.findByIdAndProductId(orderDto.optionId(), productId)
            .orElseThrow(() -> new OptionNotFoundException("옵션이 없슴다."));

        if(wishRepository.findByMemberIdAndProductId(memberId, productId).isPresent()) {
            wishRepository.delete(wishRepository.findByMemberIdAndProductId(memberId, productId).get());
        }

        optionService.subtractOptionQuantity(orderDto.optionId(),productId, orderDto.quantity());

        Order order = orderRepository.save(orderMapper.toEntity(orderDto, option));

        return orderMapper.toDto(order);
    }
}
