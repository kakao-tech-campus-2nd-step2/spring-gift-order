package gift.service;

import gift.domain.Option;
import gift.dto.MemberDTO;
import gift.dto.OrderOptionDTO;
import gift.exception.NoSuchOptionException;
import gift.repository.OptionRepository;
import gift.repository.OrderOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderOptionService {

    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final OrderOptionRepository orderOptionRepository;

    @Autowired
    public OrderOptionService(OptionService optionService, OptionRepository optionRepository, OrderOptionRepository orderOptionRepository) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.orderOptionRepository = orderOptionRepository;
    }

    public OrderOptionDTO order(MemberDTO memberDTO, OrderOptionDTO orderOptionDTO) {
        optionService.buyOption(orderOptionDTO.optionId(), orderOptionDTO.quantity());
        Option option = optionRepository.findById(orderOptionDTO.optionId())
            .orElseThrow(NoSuchOptionException::new);
        return orderOptionRepository.save(orderOptionDTO.toEntity(option, memberDTO.toEntity())).toDTO();
    }
}
