package gift.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOption> productOption = new ArrayList<>();

    public Option() {
    }

    public Option(OptionDTO optionDTO) {
        this.name = optionDTO.getName();
        this.quantity = optionDTO.getQuantity();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setOptionDTO(OptionDTO optionDTO) {
        this.name = optionDTO.getName();
        this.quantity = optionDTO.getQuantity();
    }

    public void subtract(int quantity) {
        this.quantity -= quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
