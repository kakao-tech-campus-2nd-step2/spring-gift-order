package gift.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table
public class Orders {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany
  @JoinTable(name = "option_orders")
  private List<Option> options;
  private int quantity;

  private String orderDateTime;
  private String message;

  public Orders(Long id, List<Option> options, int quantity, String orderDateTime, String message) {
    this.id = id;
    this.options = options;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
  }

  protected Orders() {
  }

  public Long getId() {
    return id;
  }

  public List<Option> getOptions() {
    return options;
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
