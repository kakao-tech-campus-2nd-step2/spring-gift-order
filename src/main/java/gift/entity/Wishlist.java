package gift.entity;

import gift.dto.WishlistResponse;
import gift.exception.InvalidOptionException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wishlist")
public class Wishlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Column(nullable = false)
	private int quantity;
	
	public Wishlist() {}
	
	public Wishlist(User user, Product product) {
		this.user = user;
		this.product = product;
	}
	
	public Long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		if (quantity < 0) {
			throw new InvalidOptionException("Quantity cannot be less than ZERO.");
		}
		this.quantity = quantity;
	}
	
	public WishlistResponse toDto() {
        WishlistResponse dto = new WishlistResponse(this.id, this.product.getId(),
        		this.product.getName(), this.quantity);
        return dto;
    }
}