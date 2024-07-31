package gift.permission.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    // kakao login을 도입한 후 userId만으로 식별. 이외의 정보는 다른 서비스가 보관
    // 만약 id가 123456이고, kakao에게서 받았다면 id = "K123456"처럼 지정할 것.
    @Id
    private String userId;

    private boolean isAdmin;

    // 토큰 관련 필드가 많아지면 Embeddable로 추출할 것.
    // 카카오 access token을 refresh token으로 사용.
    private String refreshToken;

    // refresh token의 만료 기간을 따로 저장.
    // refresh token이 뚫리면 이 값을 폐기시키면 안심.
    private Date refreshTokenExpiry;

    protected User() {

    }

    public User(String userId, boolean isAdmin, String refreshToken, int expiry) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = new Date(System.currentTimeMillis() + secondToMillis(expiry));
    }

    // 언젠간 쓸 메서드들
    public void grantAdmin() {
        this.isAdmin = true;
    }

    public void revokeAdmin() {
        this.isAdmin = false;
    }

    // 로그인 == 카카오에 api 요청해서 리프레시 토큰 재발급
    public void login(String refreshToken, int expiry) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiry = new Date(System.currentTimeMillis() + secondToMillis(expiry));
    }

    public String getUserId() {
        return userId;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Date getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    private long secondToMillis(int second) {
        return second * 1000L;
    }
}