package gift.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "loginType"}))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String loginType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TokenAuth tokenAuth;

    public Member() {
    }

    public Member(String email, String password, String loginType) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setTokenAuth(TokenAuth tokenAuth) {
        this.tokenAuth = tokenAuth;
    }

    public TokenAuth getTokenAuth() {
        return tokenAuth;
    }
}
