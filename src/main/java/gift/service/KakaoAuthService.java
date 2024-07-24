package gift.service;

public interface KakaoAuthService {
    String getAccessToken(String authorizationCode);
}