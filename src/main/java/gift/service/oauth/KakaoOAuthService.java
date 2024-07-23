package gift.service.oauth;

import gift.config.KakaoProperties;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class KakaoOAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestClient client;

    public KakaoOAuthService(KakaoProperties kakaoProperties, RestClient client) {
        this.kakaoProperties = kakaoProperties;
        this.client = client;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoProperties.clientId());
        requestBody.add("redirect_uri", kakaoProperties.redirectUrl());
        requestBody.add("code", code);

        try {
            ResponseEntity<Map> response = client.post()
                .uri(URI.create(kakaoTokenUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .toEntity(Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                String accessToken = (String) responseBody.get("access_token");
                Integer expiresIn = (Integer) responseBody.get("expires_in");
                String refreshToken = (String) responseBody.get("refresh_token");
                Integer refreshTokenExpiresIn = (Integer) responseBody.get("refresh_token_expires_in");

                return new KakaoTokenResponse(accessToken, expiresIn, refreshToken, refreshTokenExpiresIn);
            } else {
                throw new RuntimeException("토큰 응답이 올바르지 않습니다.");
            }
        } catch (RestClientResponseException e) {
            throw new RuntimeException("토큰 발급에 실패했습니다.", e);
        }
    }

    public KakaoUnlinkResponse unlinkUser(String accessToken) {
        String kakaoUnlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        try {
            ResponseEntity<Map> response = client.post()
                .uri(URI.create(kakaoUnlinkUrl))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                Long id = ((Number) responseBody.get("id")).longValue();
                return new KakaoUnlinkResponse(id);
            } else {
                throw new RuntimeException("연결 끊기 응답이 올바르지 않습니다.");
            }
        } catch (RestClientResponseException e) {
            throw new RuntimeException("연결 끊기에 실패했습니다.", e);
        }
    }

    public KakaoScopeResponse getUserScopes(String accessToken) {
        String kakaoScopesUrl = "https://kapi.kakao.com/v2/user/scopes";

        try {
            ResponseEntity<KakaoScopeResponse> response = client.get()
                .uri(URI.create(kakaoScopesUrl))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(KakaoScopeResponse.class);

            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new RuntimeException("동의 내역 가져오기에 실패했습니다.", e);
        }
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            ResponseEntity<Map> response = client.get()
                .uri(URI.create(kakaoUserInfoUrl))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                Long id = ((Number) responseBody.get("id")).longValue();
                Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
                String nickname = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
                String email = (String) kakaoAccount.get("email");

                return new KakaoUserResponse(id, nickname, email);
            } else {
                throw new RuntimeException("사용자 정보 응답이 올바르지 않습니다.");
            }
        } catch (RestClientResponseException e) {
            throw new RuntimeException("사용자 정보 가져오기에 실패했습니다.", e);
        }
    }
}
