package gift.users.user;

import gift.users.wishlist.WishList;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String kakaoId;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WishList> wishes = new ArrayList<>();

    public User() {
    }

    public User(String kakaoId) {
        this.kakaoId = kakaoId;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public List<WishList> getWishes() {
        return wishes;
    }

    public void addWishList(WishList wishList) {
        this.wishes.add(wishList);
        wishList.setUser(this);
    }

    public void removeWishList(WishList wishList) {
        wishes.remove(wishList);
        wishList.setUser(null);
    }
}
