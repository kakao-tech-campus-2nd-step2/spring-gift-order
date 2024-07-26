package gift.dto;

public class OrderRequest {
    private final Long optionId;
    private final int quantity;
    private final String message;

    private OrderRequest(Builder builder) {
        this.optionId = builder.optionId;
        this.quantity = builder.quantity;
        this.message = builder.message;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        private Long optionId;
        private int quantity;
        private String message;

        public Builder optionId(Long optionId) {
            this.optionId = optionId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public OrderRequest build() {
            return new OrderRequest(this);
        }
    }
}