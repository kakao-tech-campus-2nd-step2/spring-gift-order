package gift.model.token;

import gift.model.user.User;
import jakarta.persistence.*;

@Entity
public class KakaoToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private String accessToken;

    protected KakaoToken() {
    }

    public KakaoToken(User user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
