package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.KakaoProperties;
import gift.OAuthToken;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoApiService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";

    public KakaoApiService(KakaoProperties kakaoProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private static HttpHeaders makeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> makeBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.kakaoClientId());
        body.add("redirect_uri", kakaoProperties.kakaoRedirectUrl());
        body.add("code", code);
        return body;
    }

    private OAuthToken parseOAuthToken(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, OAuthToken.class);
    }

    public String getAccessToken(String code) {
        HttpHeaders headers = makeHeaders();
        MultiValueMap<String, String> body = makeBody(code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    TOKEN_REQUEST_URI,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            OAuthToken oauthToken = parseOAuthToken(response.getBody());

            return oauthToken.getAccess_token();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw ex; // 예외를 던져 GlobalExceptionHandler에서 처리
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON response", e);
        }
    }

}
