package gift.users.kakao;

import gift.error.KakaoAuthenticationException;
import gift.users.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoAuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KakaoProperties kakaoProperties;
    private final RestClient.Builder restClientBuilder;
    private final UserService userService;

    private static final String HEADER_NAME = "Authorization";

    public KakaoAuthService(KakaoProperties kakaoProperties, RestClient.Builder restClientBuilder,
        UserService userService) {
        this.kakaoProperties = kakaoProperties;
        this.restClientBuilder = restClientBuilder;
        this.userService = userService;
    }

    public String getKakaoLoginUrl() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(
                kakaoProperties.authUrl())
            .queryParam("response_type", kakaoProperties.loginResponseType())
            .queryParam("client_id", kakaoProperties.clientId())
            .queryParam("redirect_uri", kakaoProperties.redirectUri());
        return uriComponentsBuilder.toUriString();
    }

    public String kakaoCallBack(String code) {
        String token = getKakaoToken(code);
        Long userId = getKakaoUser(token);
        return userService.loginGiveToken(userId.toString());
    }

    private Long getKakaoUser(String token) {
        logger.info("Getting kakao userInfo with token: {}", token);

        ResponseEntity<KakaoUserDTO> response;
        try {
            response = restClientBuilder.build().post()
                .uri(kakaoProperties.userUrl())
                .header(HEADER_NAME, kakaoProperties.userHeaderValue() + " " + token)
                .retrieve()
                .toEntity(KakaoUserDTO.class);
        } catch(RestClientException e){
            logger.info("Failed to receive kakao userInfo: {}", e.getMessage());
            throw new KakaoAuthenticationException("카카오 사용자 값을 서버에서 가져오는 데에 실패했습니다.");
        }

        logger.info("Response received from kakao userInfo request: {}", response.getStatusCode());

        KakaoUserDTO kakaoUserDTO = response.getBody();

        if (kakaoUserDTO == null) {
            logger.error("Kakao userInfo response body is null.");
            throw new KakaoAuthenticationException("카카오 사용자 정보 값이 비어있습니다.");
        }

        return userService.findByKakaoIdAndRegisterIfNotExists(kakaoUserDTO.id().toString());
    }

    private String getKakaoToken(String code) {
        logger.info("Getting kakao token with code: {}", code);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoProperties.tokenGrantType());
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        ResponseEntity<KakaoTokenDTO> response;
        try {
            response = restClientBuilder.build().post()
                .uri(kakaoProperties.tokenUrl())
                .body(body)
                .retrieve()
                .toEntity(KakaoTokenDTO.class);
        } catch(RestClientException e){
            logger.info("Failed to recieve kakao token: {}", e.getMessage());
            throw new KakaoAuthenticationException("카카오 토큰 값을 서버에서 가져오는 데에 실패했습니다.");
        }

        logger.info("Response received from kakao token request: {}", response.getStatusCode());

        KakaoTokenDTO kakaoTokenDTO = response.getBody();

        if (kakaoTokenDTO == null) {
            logger.error("Kakao token response body is null.");
            throw new KakaoAuthenticationException("카카오 토큰 값이 비어있습니다.");
        }

        return kakaoTokenDTO.accessToken();
    }
}
