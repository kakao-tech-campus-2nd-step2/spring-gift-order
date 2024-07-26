package gift.service;

import gift.domain.Option;
import gift.dto.MemberDTO;
import gift.dto.OrderDTO;
import gift.exception.NoSuchOptionException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OptionService optionService, OptionRepository optionRepository, OrderRepository orderRepository) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
    }

    public OrderDTO order(MemberDTO memberDTO, OrderDTO orderDTO) {
        optionService.buyOption(orderDTO.optionId(), orderDTO.quantity());
        Option option = optionRepository.findById(orderDTO.optionId())
            .orElseThrow(NoSuchOptionException::new);
        return orderRepository.save(orderDTO.toEntity(option, memberDTO.toEntity())).toDTO();
    }
}
