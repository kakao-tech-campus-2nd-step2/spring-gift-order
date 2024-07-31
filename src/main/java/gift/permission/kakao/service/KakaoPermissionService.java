package gift.permission.kakao.service;

import static gift.permission.util.PlatformCodeUtil.KAKAO_CODE;

import gift.global.dto.TokenDto;
import gift.permission.kakao.component.KakaoProperties;
import gift.permission.kakao.dto.KakaoIdDto;
import gift.permission.kakao.dto.KakaoTokenDto;
import gift.permission.user.service.UserService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoPermissionService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final UserService userService;

    @Autowired
    public KakaoPermissionService(KakaoProperties kakaoProperties,
        UserService userService) {
        this.kakaoProperties = kakaoProperties;
        this.userService = userService;
        restClient = RestClient.builder().build();
    }

    // 카카오 로그인 step1에 해당하는 메서드
    public RedirectView kakaoAuthorize() {
        var url = "https://kauth.kakao.com/oauth/authorize";
        var targetUri = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("client_id", kakaoProperties.clientId())
            .queryParam("redirect_uri", kakaoProperties.redirectUri())
            .queryParam("response_type", kakaoProperties.responseType())
            .build()
            .toUriString();

        // 에러가 있으면 알아서 카카오가 처리하는 view로 반환해줄 것.
        return new RedirectView(targetUri);
    }

    // 카카오 로그인 step2, 3에 해당하는 메서드
    public TokenDto kakaoLogin(String code) {
        // step2
        var token = getKakaoToken(code);

        // step3
        var userId = getUserId(token);
        return userService.login(userId, KAKAO_CODE, token.refreshToken(),
            token.refreshTokenExpiresIn());
    }

    // 카카오 로그인 step2에 해당하는 메서드
    private KakaoTokenDto getKakaoToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var targetUri = URI.create(url);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", kakaoProperties.grantType());
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        var response = restClient.post().uri(targetUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoTokenDto.class);

        return response.getBody();
    }

    // token을 받아온 후, token을 사용하여 식별자로 사용할 id를 받아오는 메서드 (step3에서 사용)
    private long getUserId(KakaoTokenDto kakaoTokenDto) {
        var tokenType = kakaoTokenDto.tokenType();
        var accessToken = kakaoTokenDto.accessToken();
        var authorization = tokenType + " " + accessToken;

        var url = "https://kapi.kakao.com/v2/user/me";
        var targetUri = URI.create(url);

        var response = restClient.post().uri(targetUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(HttpHeaders.AUTHORIZATION, authorization)
            .retrieve()
            .toEntity(KakaoIdDto.class);

        return response.getBody().id();
    }
}
