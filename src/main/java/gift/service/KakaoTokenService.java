package gift.service;

import gift.exception.ForbiddenException;
import gift.exception.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoTokenService {

    @Value("${kakao.app.key}")
    private String appKey;

    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";
    private final RestTemplate restTemplate;

    @Autowired
    public KakaoTokenService(RestTemplateBuilder restTemplateBuilder, RestTemplateResponseErrorHandler errorHandler) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(errorHandler) // 사용자 정의 오류 처리기 주입
                .build();
    }

    public String getAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", appKey);
        params.add("redirect_uri", "http://localhost:8080/callback");
        params.add("code", authorizationCode);

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
            return response.getBody(); // Access Token 반환
        } catch (ForbiddenException e) {
            System.err.println("엑세스토큰 처리시, 접근이 거부되었습니다: " + e.getMessage());
            return null;
        } catch (HttpClientErrorException e) {
            System.err.println("엑세스토큰 처리시, 클라이언트 오류가 발생했습니다: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("엑세스토큰 처리시, 문제가 발생했습니다: " + e.getMessage());
            return null;
        }
    }
}

