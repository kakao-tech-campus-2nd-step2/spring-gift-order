package gift.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.util.dto.KakaoInfoDto;
import gift.util.dto.KakaoTokenDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoApiUtil {
    private final ObjectMapper objectMapper;
    private final RestClient client;
    private final KakaoProperties properties;

    public KakaoApiUtil(ObjectMapper objectMapper, KakaoProperties properties) {
        this.objectMapper = objectMapper;
        this.client = RestClient.builder().build();
        this.properties = properties;
    }

    public KakaoTokenDto getKakaoAccessToken(String code, String redirectUrl) {
        return client.post()
                .uri(URI.create(properties.tokenUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createBodyForAccessToken(code, redirectUrl))
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
                    }
                    throw new AuthenticationException("Kakao login failed");
                });
    }

    public String getKakaoMemberInfo(String accessToken) {
        return client.post()
                .uri(URI.create(properties.memberInfoUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoInfoDto.class).id() + "@Kakao";
                    }
                    throw new AuthenticationException("Kakao Info failed");
                });
    }

    public KakaoTokenDto refreshAccessToken(String refreshToken) {
        return client.post()
                .uri(URI.create(properties.refreshUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createBodyForRefreshAccessToken(refreshToken))
                .exchange((request, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
                    }
                    throw new AuthenticationException("kakao token refresh failed");
                });
    }

    public void signOutKakao(String accessToken) {
        client.post()
                .uri(URI.create(properties.logoutUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        (request, response) -> {
                            throw new AuthenticationException("Logout failed");
                        }
                );

    }

    private @NotNull LinkedMultiValueMap<String, String> createBodyForAccessToken(String code, String redirectUrl) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", redirectUrl);
        body.add("code", code);
        return body;
    }

    private @NotNull LinkedMultiValueMap<String, String> createBodyForRefreshAccessToken(String refreshToken) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", properties.clientId());
        body.add("refresh_token", refreshToken);
        return body;
    }
}
