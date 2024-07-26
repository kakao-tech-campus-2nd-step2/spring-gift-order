package gift.doamin.user.service;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.doamin.user.dto.KakaoOAuthTokenResponseDto;
import gift.doamin.user.dto.KakaoOAuthUserInfoResponseDto;
import gift.doamin.user.entity.RefreshToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.properties.KakaoClientProperties;
import gift.doamin.user.properties.KakaoProviderProperties;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.user.repository.RefreshTokenRepository;
import gift.doamin.user.util.AuthorizationOAuthUriBuilder;
import gift.global.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OAuthService {

    private final KakaoClientProperties clientProperties;
    private final KakaoProviderProperties providerProperties;
    private final JpaUserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public OAuthService(KakaoClientProperties clientProperties,
        KakaoProviderProperties providerProperties, JpaUserRepository userRepository,
        JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository) {
        this.clientProperties = clientProperties;
        this.providerProperties = providerProperties;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String getAuthUrl() {
        return new AuthorizationOAuthUriBuilder()
            .clientProperties(clientProperties)
            .providerProperties(providerProperties)
            .build();
    }

    public String getAccessToken(String authorizeCode) {
        RestClient restClient = RestClient.builder().build();

        String tokenUri = providerProperties.tokenUri();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizeCode);
        body.add("redirect_uri", clientProperties.redirectUri());
        body.add("client_id", clientProperties.clientId());
        body.add("client_secret", clientProperties.clientSecret());

        ResponseEntity<KakaoOAuthTokenResponseDto> entity = restClient.post()
            .uri(tokenUri)
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoOAuthTokenResponseDto.class);

        return entity.getBody().getAccessToken();
    }

    public String authenticate(String accessToken) {
        RestClient restClient = RestClient.builder().build();

        ResponseEntity<KakaoOAuthUserInfoResponseDto> entity = restClient.get()
            .uri(providerProperties.userInfoUri())
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .retrieve()
            .toEntity(KakaoOAuthUserInfoResponseDto.class);

        Long id = entity.getBody().getId();
        String nickname = entity.getBody().getProperties().get("nickname");
        String email = id + "@kakao.oauth";

        User user = userRepository.findByEmail(email)
            .orElseGet(
                () -> userRepository.save(new User(email, id.toString(), nickname, UserRole.USER))
            );

        String myRefreshToken = jwtProvider.generateRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
            .orElseGet(() -> new RefreshToken(myRefreshToken, user));
        refreshToken.setToken(myRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return myRefreshToken;
    }
}
