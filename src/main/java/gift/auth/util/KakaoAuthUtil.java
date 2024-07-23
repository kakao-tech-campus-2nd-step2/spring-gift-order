package gift.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.vo.KakaoProperties;
import gift.global.security.JwtFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class KakaoAuthUtil {

    private final KakaoProperties kakaoProperties;

    public KakaoAuthUtil(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + kakaoProperties.redirectUri() +
                "&client_id="    + kakaoProperties.clientId();
    }

    public String generateKakaoEmail(String userInfo) {
        return "kakao_user" +
                userInfo +
                "@kakao.com";
    }

    public RequestEntity<LinkedMultiValueMap<String, String>> getRequestWithPost(String url,
                                                                                 String code) {
        return RequestEntity.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getRequestBody(code));
    }

    public RequestEntity<Void> getRequestWithGet(String url,
                                                 String token) {
        return RequestEntity.get(url)
                .header(HttpHeaders.AUTHORIZATION, JwtFilter.BEAR_PREFIX + token)
                .build();
    }

    private LinkedMultiValueMap<String, String> getRequestBody(String authCode) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", kakaoProperties.grantType());
        requestBody.add("client_id", kakaoProperties.clientId());
        requestBody.add("redirect_uri", kakaoProperties.redirectUri());
        requestBody.add("code", authCode);
        return requestBody;
    }

    public String extractValueFromJson(String json,
                                       String key) throws JsonProcessingException {
        return new ObjectMapper().readTree(json)
                .get(key)
                .asText();
    }

    public String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        return Base64.getEncoder()
                .encodeToString(bytes);
    }

}
