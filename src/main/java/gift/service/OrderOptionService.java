package gift.service;

import gift.domain.Option;
import gift.dto.MemberDTO;
import gift.dto.OrderOptionDTO;
import gift.exception.NoSuchOptionException;
import gift.repository.OptionRepository;
import gift.repository.OrderOptionRepository;
import gift.repository.WishedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderOptionService {

    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final WishedProductRepository wishedProductRepository;
    private final OrderOptionRepository orderOptionRepository;

    @Autowired
    public OrderOptionService(OptionService optionService, OptionRepository optionRepository,
        WishedProductRepository wishedProductRepository, OrderOptionRepository orderOptionRepository) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.wishedProductRepository = wishedProductRepository;
        this.orderOptionRepository = orderOptionRepository;
    }

    @Transactional
    public OrderOptionDTO order(MemberDTO memberDTO, OrderOptionDTO orderOptionDTO) {
        optionService.buyOption(orderOptionDTO.optionId(), orderOptionDTO.quantity());
        Option option = optionRepository.findById(orderOptionDTO.optionId())
            .orElseThrow(NoSuchOptionException::new);
        wishedProductRepository.deleteAllByMemberAndProduct(memberDTO.toEntity(), option.getProduct());
        return orderOptionRepository.save(orderOptionDTO.toEntity(option, memberDTO.toEntity())).toDTO();
    }
}
