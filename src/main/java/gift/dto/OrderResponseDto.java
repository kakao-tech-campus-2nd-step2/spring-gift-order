package gift.dto;

import gift.entity.Order;

import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private Long optionId;
    private Long quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponseDto(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public OrderResponseDto(Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public static OrderResponseDto fromEntity(Order order) {
        return new OrderResponseDto(order.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
