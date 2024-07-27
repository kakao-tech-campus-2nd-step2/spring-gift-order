package gift.member.dto;

import java.time.LocalDateTime;

public class KakaoTokenInfo {

    private String kakaoAccessToken;

    private LocalDateTime accessTokenExpiredAt;

    private String kakaoRefreshToken;

    protected KakaoTokenInfo() {
    }

    public KakaoTokenInfo(String kakaoAccessToken,
                          LocalDateTime accessTokenExpiredAt,
                          String kakaoRefreshToken) {
        this.kakaoAccessToken = kakaoAccessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.kakaoRefreshToken = kakaoRefreshToken;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public LocalDateTime getAccessTokenExpiredAt() {
        return accessTokenExpiredAt;
    }

    public String getKakaoRefreshToken() {
        return kakaoRefreshToken;
    }

    public boolean isExpired(LocalDateTime localDateTime) {
        return accessTokenExpiredAt.isAfter(localDateTime);
    }

    public void update(KakaoTokenInfo kakaoTokenInfo) {
        this.kakaoAccessToken = kakaoTokenInfo.getKakaoAccessToken();
        this.accessTokenExpiredAt = kakaoTokenInfo.getAccessTokenExpiredAt();
        this.kakaoRefreshToken = kakaoTokenInfo.getKakaoRefreshToken();
    }

}
