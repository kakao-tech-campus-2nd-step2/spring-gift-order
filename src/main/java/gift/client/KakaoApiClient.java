package gift.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.KakaoUserResponse;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class KakaoApiClient {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public KakaoApiClient(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = new RestTemplate();
    }

    public String getAccessToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = kakaoProperties.getTokenUrl();
        String body = UriComponentsBuilder.newInstance()
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoProperties.getClientId())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .queryParam("code", authorizationCode)
                .build().toUriString().substring(1);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.KAKAO_AUTH_FAILED, "응답 코드: " + response.getStatusCode());
        }

        return extractAccessToken(response.getBody());
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                kakaoProperties.getInfoUrl(),
                HttpMethod.GET,
                request,
                KakaoUserResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.KAKAO_AUTH_FAILED, "사용자 정보 조회 실패: " + response.getStatusCode());
        }

        return response.getBody();
    }

    private String extractAccessToken(String responseBody) {
        JsonNode jsonNode = parseJson(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private JsonNode parseJson(String body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(body);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.KAKAO_AUTH_FAILED, "JSON 파싱 오류: " + e.getMessage());
        }
    }
}
