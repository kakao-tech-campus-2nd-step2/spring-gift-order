package gift.DTO;

import java.util.List;

public class OrderDto {

  private final Long id;
  private final List<OptionDto> optionDtos;
  private final int quantity;
  private final String orderDateTime;
  private final String message;

  public OrderDto(Long id, List<OptionDto> optionDtos, int quantity, String orderDateTime,
    String message) {
    this.id = id;
    this.optionDtos = optionDtos;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  public List<OptionDto> getOptionDtos() {
    return optionDtos;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getOrderDateTime() {
    return orderDateTime;
  }

  public String getMessage() {
    return message;
  }
}
