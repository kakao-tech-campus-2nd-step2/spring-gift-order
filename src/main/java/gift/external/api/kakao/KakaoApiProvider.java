package gift.external.api.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.external.api.kakao.client.KakaoApiClient;
import gift.external.api.kakao.client.KakaoAuthClient;
import gift.external.api.kakao.dto.FeedObjectRequest;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoApiProvider {

    private final KakaoProperties kakaoProperties;
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;
    private final ObjectMapper objectMapper;

    private static final String[] SCOPE = { "profile_nickname", "talk_message", "account_email" };
    private static final String RESPONSE_TYPE = "code";

    public KakaoApiProvider(
        KakaoProperties kakaoProperties,
        KakaoAuthClient kakaoAuthClient,
        KakaoApiClient kakaoApiClient,
        ObjectMapper objectMapper
    ) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoApiClient = kakaoApiClient;
        this.objectMapper = objectMapper;
    }

    public String getAuthCodeUrl() {
        return kakaoProperties.authBaseUrl()
            + "?scope=" + String.join(",", SCOPE)
            + "&response_type=" + RESPONSE_TYPE
            + "&redirect_uri=" + kakaoProperties.redirectUri();
    }

    public KakaoToken getToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        return kakaoAuthClient.getAccessToken(body);
    }

    public void validateAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        kakaoApiClient.getAccessTokenInfo(headers);
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return kakaoApiClient.getUserInfo(headers);
    }

    public KakaoToken renewToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.clientId());
        body.add("refresh_token", refreshToken);

        return kakaoAuthClient.getAccessToken(body);
    }

    public Integer sendMessageToMe(String accessToken, FeedObjectRequest templateObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        try {
            body.add("template_object", objectMapper.writeValueAsString(templateObject));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonResponse = kakaoApiClient.sendMessageToMe(headers, body);
        return Integer.parseInt(jsonResponse.get("result_code").asText());
    }
}
