package gift.login;

import static org.springframework.http.HttpStatus.FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class OauthController {

    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<Void> kakaoLogin() {
        return ResponseEntity.status(FOUND)
            .location(oauthService.getKakaoLoginURL())
            .build();
    }

    @GetMapping("/kakao/token")
    public String getToken(@RequestParam String code) {
        return oauthService.getTokenFromKakao(code);
    }
}
