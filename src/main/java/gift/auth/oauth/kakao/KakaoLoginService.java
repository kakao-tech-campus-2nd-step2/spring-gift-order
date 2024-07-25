package gift.auth.oauth.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import gift.auth.dto.Token;
import gift.client.KakaoApiClient;
import gift.client.KakaoAuthClient;
import gift.domain.user.dto.UserDto;
import gift.domain.user.dto.UserLoginDto;
import gift.domain.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KakaoLoginService {

    private final KakaoProperties kakaoProperties;
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;
    private final UserService userService;

    private static final String[] SCOPE = { "profile_nickname", "talk_message", "account_email" };
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";

    public KakaoLoginService(KakaoProperties kakaoProperties, UserService userService,
        KakaoAuthClient kakaoAuthClient, KakaoApiClient kakaoApiClient) {
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoApiClient = kakaoApiClient;
    }

    public String getAuthCodeUrl() {
        return kakaoProperties.authBaseUrl()
            + "?scope=" + String.join(",", SCOPE)
            + "&response_type=" + RESPONSE_TYPE
            + "&redirect_uri=" + kakaoProperties.redirectUri();
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
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        JsonNode response = kakaoAuthClient.getAccessToken(body);

        return response.get("access_token").asText();
    }

    private void validateAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        kakaoApiClient.getAccessTokenInfo(headers);
    }

    private Map<String, String> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        JsonNode response = kakaoApiClient.getUserInfo(headers);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("name", response.get("kakao_account").get("profile").get("nickname").asText());
        userInfo.put("email", response.get("kakao_account").get("email").asText());

        return userInfo;
    }
}
