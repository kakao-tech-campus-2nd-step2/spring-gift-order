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

@Component
public class KakaoAuthUtil {

    private final KakaoProperties kakaoProperties;

    public KakaoAuthUtil(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + kakaoProperties.getRedirectUri() +
                "&client_id="    + kakaoProperties.getClientId();
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
        requestBody.add("grant_type", kakaoProperties.getGrantType());
        requestBody.add("client_id", kakaoProperties.getClientId());
        requestBody.add("redirect_uri", kakaoProperties.getRedirectUri());
        requestBody.add("code", authCode);
        return requestBody;
    }

    public String getValueOfJsonByKey(String json,
                                      String key) throws JsonProcessingException {
        return new ObjectMapper().readTree(json)
                .get(key)
                .asText();
    }

}
