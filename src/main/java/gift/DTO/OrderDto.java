package gift.DTO;

public class OrderDto {

  private Long id;
  private OptionDto optionDto;
  private int quantity;
  private String orderDateTime;
  private String message;

  public OrderDto(Long id, OptionDto optionDto, int quantity, String orderDateTime, String message){
    this.id=id;
    this.optionDto=optionDto;
    this.quantity=quantity;
    this.orderDateTime=orderDateTime;
    this.message=message;
  }

  public Long getId() {
    return id;
  }

  public OptionDto getOptionDto() {
    return optionDto;
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
