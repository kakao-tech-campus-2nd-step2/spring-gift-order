package gift.service;

import gift.dto.KakaoProperties;
import java.util.Map;
import java.net.URI;
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

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize" + "?scope=talk_message" + "&response_type=code"
            + "&redirect_uri=" + kakaoProperties.getRedirectUrl()
            + "&client_id=" + kakaoProperties.getClientId();
    }
    // https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code
    // &redirect_uri={kakaoRedirectUrl}&client_id={kakaoClientId}

    public Map<String, Object> getAccessToken(String code) {
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
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);
        return body;
    }

    private Map<String, Object> extractAccessToken(ResponseEntity<Map> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "access token 발급 실패 : HTTP status " + response.getStatusCode());
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || responseBody.get("access_token") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "No access token was found in the response.");
        }

        return responseBody;
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        String url = KAKAO_API_URI + "/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(URI.create(url), HttpMethod.GET,
                request, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error while fetching user info", e);
        }
    }
}
