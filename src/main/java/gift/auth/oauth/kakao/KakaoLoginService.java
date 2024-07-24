package gift.auth.oauth.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.dto.Token;
import gift.domain.user.dto.UserDto;
import gift.domain.user.dto.UserLoginDto;
import gift.domain.user.service.UserService;
import gift.exception.OauthLoginException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final KakaoProperties kakaoProperties;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    private static final RestClient restClient = RestClient.create();

    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private static final String TOKEN_INFO_REQUEST_URL = "https://kapi.kakao.com/v1/user/access_token_info";
    private static final String USER_INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoLoginService(KakaoProperties kakaoProperties, UserService userService, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    public Token login(String code) {
        String accessToken = getAccessToken(code);
        validateAccessToken(accessToken);
        Map<String, String> userInfo = getUserInfo(accessToken);

        String email = userInfo.get("email");
        Optional<UserDto> userDto = userService.findByEmail(email);

        if (userDto.isEmpty()) {
            return signUp(userInfo);
        }

        return userService.login(new UserLoginDto(email, userDto.get().password()));
    }

    private Token signUp(Map<String, String> userInfo) {
        UserDto userDto = new UserDto(null, userInfo.get("name"), userInfo.get("email"), "kakao", null);
        return userService.signUp(userDto);
    }

    private String getAccessToken(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        JsonNode jsonResponse = restClient.post()
            .uri(URI.create(TOKEN_REQUEST_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .accept(MediaType.APPLICATION_JSON)
            .exchange((request, response) -> {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new OauthLoginException(response.toString());
                }
                return objectMapper.readTree(response.getBody());
            });

        return jsonResponse.get("access_token").asText();
    }

    private void validateAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        restClient.get()
            .uri(URI.create(TOKEN_INFO_REQUEST_URL))
            .headers(httpHeaders -> {
                httpHeaders.addAll(headers);
            })
            .accept(MediaType.APPLICATION_JSON)
            .exchange((request, response) -> {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new OauthLoginException(response.toString());
                }
                return response.getBody().toString();
            });
    }

    private Map<String, String> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        JsonNode jsonResponse = restClient.get()
            .uri(URI.create(USER_INFO_REQUEST_URL))
            .headers(httpHeaders -> {
                httpHeaders.addAll(headers);
            })
            .accept(MediaType.APPLICATION_JSON)
            .exchange((request, response) -> {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new OauthLoginException(response.toString());
                }
                return objectMapper.readTree(response.getBody());
            });

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("name", jsonResponse.get("kakao_account").get("profile").get("nickname").asText());
        userInfo.put("email", jsonResponse.get("kakao_account").get("email").asText());

        return userInfo;
    }
}
