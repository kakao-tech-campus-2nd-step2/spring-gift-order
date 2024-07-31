package gift.wishlist.entity;

import gift.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// wishlist 객체. quantity는 없앴습니다.
@Entity
@Table(name = "wish_products", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "product_id"})})
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishListId;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected WishList() {
    }

    public WishList(Long wishListId, String userId, Product product) {
        this.wishListId = wishListId;
        this.userId = userId;
        this.product = product;
    }

    public WishList(String userId, Product product) {
        this(null, userId, product);
    }

    public Long getWishListId() {
        return wishListId;
    }

    public String getUserId() {
        return userId;
    }

    public Product getProduct() {
        return product;
    }
}
