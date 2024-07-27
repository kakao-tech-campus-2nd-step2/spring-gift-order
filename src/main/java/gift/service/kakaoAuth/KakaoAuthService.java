package gift.service.kakaoAuth;

import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;

    public KakaoAuthService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getKakaoAuthUrl() {
        StringBuffer str = new StringBuffer();
        str.append("https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code");
        str.append("&redirect_uri=" + kakaoProperties.redirectUri());
        str.append("&client_id=" + kakaoProperties.clientId());

        return str.toString();
    }

}
