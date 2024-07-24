package gift.service;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoUserResponse;
import gift.entity.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public KakaoAuthService(KakaoProperties kakaoProperties, KakaoApiClient kakaoApiClient, UserRepository userRepository, TokenService tokenService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiClient = kakaoApiClient;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String getKakaoLoginUrl() {
        return kakaoProperties.getAuthUrl() + "?response_type=code&client_id=" + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri();
    }

    public Map<String, String> handleKakaoCallback(String authorizationCode) {
        String accessToken = kakaoApiClient.getAccessToken(authorizationCode);
        KakaoUserResponse kakaoUser = kakaoApiClient.getUserInfo(accessToken);

        User user = userRepository.findByKakaoId(kakaoUser.getId().toString())
                .orElseGet(() -> userRepository.save(new User(kakaoUser.getId(), kakaoUser.getProperties().getNickname())));

        String jwtToken = tokenService.generateToken(kakaoUser.getId().toString(), "kakao");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("kakaoAccessToken", accessToken);
        tokens.put("jwtToken", jwtToken);

        return tokens;
    }
}
