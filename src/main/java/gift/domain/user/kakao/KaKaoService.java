package gift.domain.user.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.user.JpaUserRepository;
import gift.domain.user.User;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KaKaoService {
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final KaKaoProperties kaKaoProperties;
    private final JpaUserRepository userRepository;

    @Autowired
    public KaKaoService(
        JpaUserRepository userRepository,
        RestTemplateBuilder restTemplateBuilder,
        ObjectMapper objectMapper,
        KaKaoProperties kaKaoProperties
    ) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.kaKaoProperties = kaKaoProperties;
    }

    public KaKaoToken getKaKaoToken(String authorizedCode) {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kaKaoProperties.clientId());
        body.add("redirect_uri", kaKaoProperties.redirectUrl());
        body.add("code", authorizedCode);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(TOKEN_URL));

        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return parseTokenResponse(response);
        } else {
            throw new BusinessException(ErrorCode.FORBIDDEN, "사용자 권한이 없습니다.");
        }
    }

    public User findUserByKaKaoAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return parseUserInfo(response);
        }
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "사용자 정보 조회 중 오류 발생");
    }

    private KaKaoToken parseTokenResponse(ResponseEntity<String> response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String accessToken = rootNode.path("access_token").asText();
            String refreshToken = rootNode.path("refresh_token").asText();
            return new KaKaoToken(accessToken, refreshToken);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "토큰을 처리하는 중 오류가 발생했습니다.");
        }
    }

    private User parseUserInfo(ResponseEntity<String> response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String id = rootNode.path("id").asText(); // unique
            String nickname = rootNode.path("properties").path("nickname").asText();
            String email = rootNode.path("kakao_account").path("email").asText();

            User findUser = userRepository.findByEmail(email);
            if (findUser == null) {
                findUser = createUser(email);
            }

            return findUser;
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "json 데이터 형식이 올바르지 않습니다.");
        }
    }

    private User createUser(String email) {
        String salt = UUID.randomUUID().toString();
        String ori_password = salt + UUID.randomUUID().toString();
        String hashed_password = ori_password; // hash 과정 생략

        User user = new User(email, hashed_password);
        userRepository.save(user);
        return userRepository.findByEmail(email);
    }

    public String buildLoginPageUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
            .queryParam("client_id", kaKaoProperties.clientId())
            .queryParam("redirect_uri", kaKaoProperties.redirectUrl())
            .queryParam("response_type", "code")
            .build()
            .toUriString();
    }
}