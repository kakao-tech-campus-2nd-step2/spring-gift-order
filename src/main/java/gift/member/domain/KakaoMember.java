package gift.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "kakao_members")
public class KakaoMember {
    @Id
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expires_in")
    private int expiresIn;

    @Column(name = "scope")
    private String scope;

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public KakaoMember() {
    }

    public KakaoMember(String accessToken, String tokenType, String refreshToken, int expiresIn, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        KakaoMember item = (KakaoMember) o;
        return Objects.equals(accessToken, item.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }
}
