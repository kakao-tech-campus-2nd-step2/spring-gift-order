package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public KakaoAuthService(KakaoProperties kakaoProperties, UserRepository userRepository, TokenService tokenService) {
        this.kakaoProperties = kakaoProperties;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getKakaoLoginUrl() {
        return kakaoProperties.getAuthUrl() + "?response_type=code&client_id=" + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri();
    }

    public Map<String, String> handleKakaoCallback(String authorizationCode) {
        String accessToken = getAccessToken(authorizationCode);
        User kakaoUser = getKakaoUser(accessToken);

        String jwtToken = tokenService.generateToken(kakaoUser.getKakaoId(), "kakao");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("kakaoAccessToken", accessToken);
        tokens.put("jwtToken", jwtToken);

        return tokens;
    }

    private String getAccessToken(String authorizationCode) {
        HttpHeaders headers = createHeaders();
        MultiValueMap<String, String> body = createRequestBody(authorizationCode);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(kakaoProperties.getTokenUrl(), request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.KAKAO_AUTH_FAILED, "응답 코드: " + response.getStatusCode());
        }

        return extractAccessToken(response.getBody());
    }

    private User getKakaoUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(ErrorCode.KAKAO_AUTH_FAILED, "사용자 정보 조회 실패: " + response.getStatusCode());
        }

        JsonNode jsonNode = parseJson(response.getBody());
        Long kakaoId = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return userRepository.findByKakaoId(kakaoId.toString()).orElseGet(() -> userRepository.save(new User(kakaoId, nickname)));
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> createRequestBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", authorizationCode);
        return body;
    }

    private String extractAccessToken(String responseBody) {
        JsonNode jsonNode = parseJson(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private JsonNode parseJson(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.KAKAO_AUTH_FAILED, "JSON 파싱 오류: " + e.getMessage());
        }
    }
}
