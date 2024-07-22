package gift.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.vo.KakaoProperties;
import gift.global.security.JwtFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;

    public KakaoAuthService(RestClient restClient,
                            KakaoProperties kakaoProperties) {
        this.restClient = restClient;
        this.kakaoProperties = kakaoProperties;
    }

    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code" +
                "&redirect_uri=" + kakaoProperties.getRedirectUri() +
                "&client_id="    + kakaoProperties.getClientId();
    }

    public String getAccessToken(String authCode) throws Exception {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String responseJson = getResponseJsonWithPost(
                tokenUrl,
                getRequestBodyOfKakaoToken(authCode)
        );

        return getValueByKeyOfJson(responseJson, "access_token");
    }

    public String getUserInfo(String token) throws JsonProcessingException {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        String responseJson = getResponseJsonWithGet(userInfoUrl, token);

        return getValueByKeyOfJson(responseJson, "id");
    }

    private String getValueByKeyOfJson(String json,
                                       String key) throws JsonProcessingException {
        return new ObjectMapper().readTree(json)
                .get(key)
                .asText();
    }

    private LinkedMultiValueMap<String, String> getRequestBodyOfKakaoToken(String authCode) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", kakaoProperties.getGrantType());
        requestBody.add("client_id", kakaoProperties.getClientId());
        requestBody.add("redirect_uri", kakaoProperties.getRedirectUri());
        requestBody.add("code", authCode);
        return requestBody;
    }

    private String getResponseJsonWithPost(String url,
                                           LinkedMultiValueMap<String, String> requestBody) {
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

    private String getResponseJsonWithGet(String url, String token) {
        return restClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, JwtFilter.BEAR_PREFIX + token)
                .retrieve()
                .body(String.class);
    }

}
