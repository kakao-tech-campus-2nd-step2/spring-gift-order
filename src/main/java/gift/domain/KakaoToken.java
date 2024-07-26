package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "kakao_tokens")
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String token;

    public KakaoToken() {}

    public KakaoToken(String userEmail, String token) {
        this.userEmail = userEmail;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userId) {
        this.userEmail = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}