package gift.member.oauth.model;

import gift.member.model.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OauthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", nullable = false)
    private String provider;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "access_token", nullable = false)
    private String accessToken;
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected OauthToken() {

    }

    public OauthToken(String provider, String email, String accessToken, String refreshToken,
        Member member) {
        this.provider = provider;
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Member getMember() {
        return member;
    }
  
    public void updateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        if (refreshToken != null) {
            this.refreshToken = refreshToken;
        }
    }
}
