package gift.users.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.users.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public KakaoAuthService(KakaoProperties kakaoProperties, ObjectMapper objectMapper,
        UserService userService) {
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.restClient = RestClient.create();
    }

    public String getKakaoLoginUrl() {
        return kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
    }

    public String kakaoCallBack(String code) throws JsonProcessingException {
        String token = getKakaoToken(code);
        Long userId = getKakaoUser(token);
        return userService.loginGiveToken(userId.toString());
    }

    private Long getKakaoUser(String token) throws JsonProcessingException {
        String response = restClient.post()
            .uri(kakaoProperties.userUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(String.class);
        Long kakaoId = parseJson(response).get("id").asLong();
        return userService.findByKakaoIdAndRegisterIfNotExists(kakaoId.toString());
    }

    private String getKakaoToken(String code) throws JsonProcessingException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        String response = restClient.post()
            .uri(kakaoProperties.tokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(String.class);
        return parseJson(response).get("access_token").asText();
    }

    private JsonNode parseJson(String body) throws JsonProcessingException {
        return objectMapper.readTree(body);
    }
}
