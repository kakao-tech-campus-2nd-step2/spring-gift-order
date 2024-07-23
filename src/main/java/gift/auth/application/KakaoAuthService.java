package gift.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.util.KakaoAuthUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService {

    private final RestTemplate restTemplate;
    private final KakaoAuthUtil kakaoAuthUtil;

    public KakaoAuthService(RestTemplate restTemplate,
                            KakaoAuthUtil kakaoAuthUtil) {
        this.restTemplate = restTemplate;
        this.kakaoAuthUtil = kakaoAuthUtil;
    }

    public String getKakaoAuthUrl() {
        return kakaoAuthUtil.getKakaoAuthUrl();
    }

    public String getAccessToken(String authCode) throws Exception {
        String url = "https://kauth.kakao.com/oauth/token";
        String responseJson = restTemplate.postForObject(
                url,
                kakaoAuthUtil.getRequestWithPost(url, authCode),
                String.class
        );

        return kakaoAuthUtil.extractValueFromJson(responseJson, "access_token");
    }

    public String getUserInfo(String token) throws JsonProcessingException {
        String url = "https://kapi.kakao.com/v2/user/me";
        String responseJson = restTemplate.exchange(
                url,
                HttpMethod.GET,
                kakaoAuthUtil.getRequestWithGet(url, token),
                String.class
        ).getBody();

        return kakaoAuthUtil.extractValueFromJson(responseJson, "id");
    }

}
