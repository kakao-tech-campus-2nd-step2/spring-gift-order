package gift.service;

import gift.domain.Option;
import gift.dto.request.OrderRequest;
import gift.exception.CustomException;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

import static gift.exception.ErrorCode.DATA_NOT_FOUND;

@Service
public class OrderService {

    private final OptionRepository optionRepository;

    public OrderService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public void order(OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId()).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        option.subtract(orderRequest.quantity());
    }
}
