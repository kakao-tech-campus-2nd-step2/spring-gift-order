package gift.service;

import java.util.Map;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoRedirectUrl() {
        return kakaoRedirectUrl;
    }

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize" + "?scope=talk_message" + "&response_type=code"
            + "&redirect_uri=" + kakaoRedirectUrl
            + "&client_id=" + kakaoClientId;
    }
    // https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code
    // &redirect_uri={kakaoRedirectUrl}&client_id={kakaoClientId}

    public String getAccessToken(String code) {
        String url = KAKAO_AUTH_URI + "/oauth/token";
        HttpHeaders headers = createHeaders();
        MultiValueMap<String, String> body = createBody(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(URI.create(url), HttpMethod.POST,
                request, Map.class);
            return extractAccessToken(response);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error while getting access token", e);
        }

    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> createBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUrl);
        body.add("code", code);
        return body;
    }

    private String extractAccessToken(ResponseEntity<Map> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "access token 발급 실패 : HTTP status " + response.getStatusCode());
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || responseBody.get("access_token") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "응답에 access token이 발견 되지 않습니다.");
        }

        return (String) responseBody.get("access_token");
    }
}
