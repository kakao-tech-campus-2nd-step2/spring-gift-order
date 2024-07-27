package gift.product.dto;

import gift.product.model.Option;
import gift.product.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class OptionDTO {

    @NotBlank
    private String name;
    @PositiveOrZero
    private int quantity;
    @NotNull
    private Long productId;

    public OptionDTO() {

    }

    public OptionDTO(String name, int quantity, Long productId) {
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Option convertToDomain(Product product) {
        return new Option(
            name,
            quantity,
            product
        );
    }

    public Option convertToDomain(Long id, Product product) {
        return new Option(
            id,
            name,
            quantity,
            product
        );
    }
}
