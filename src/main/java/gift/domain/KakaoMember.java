package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name="kakao_members")
public class KakaoMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", nullable = false)
    private  String email;
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    protected KakaoMember() {
    }

    public KakaoMember(String accessToken, String email) {
        this.email = email;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
