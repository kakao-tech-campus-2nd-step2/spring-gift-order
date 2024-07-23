package gift.controller.oauth;

import gift.config.KakaoProperties;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoOAuthController {

    private final KakaoProperties kakaoProperties;

    public KakaoOAuthController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/oauth/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        String kakaoAuthUrl =
            "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
                + kakaoProperties.redirectUrl() + "&client_id=" + kakaoProperties.clientId();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(kakaoAuthUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
