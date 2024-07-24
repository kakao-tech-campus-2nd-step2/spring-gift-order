package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.dto.OrderDTO;
import gift.product.exception.InvalidIdException;
import gift.product.model.Option;
import gift.product.repository.OptionRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionRepository optionRepository;

    @Autowired
    public OrderService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Map<String, String> orderProduct(OrderDTO orderDTO) {
        System.out.println("[OrderService] orderProduct()");
        Option option = optionRepository.findById(orderDTO.getOptionId()).orElseThrow(
            () -> new InvalidIdException(NOT_EXIST_ID)
        );
        option.subtractQuantity(orderDTO.getQuantity());
        Map<String, String> response = new HashMap<>();
        response.put("id", orderDTO.getOptionId().toString());
        response.put("quantity", String.valueOf(orderDTO.getQuantity()));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        response.put("orderDateTime", now.format(formatter));
        response.put("message", orderDTO.getMessage());
        return response;
    }

}
