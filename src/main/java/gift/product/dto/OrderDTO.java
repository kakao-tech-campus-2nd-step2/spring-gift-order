package gift.product.dto;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.exception.InvalidIdException;
import gift.product.model.Order;
import gift.product.repository.OptionRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderDTO {
    @NotNull
    private Long optionId;
    @Positive
    private int quantity;
    private String message;

    public OrderDTO(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order convert(OptionRepository optionRepository) {
        return new Order(
            optionRepository.findById(optionId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID)),
            quantity,
            message
        );
    }

}
