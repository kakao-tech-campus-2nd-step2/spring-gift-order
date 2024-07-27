package gift.Controller;

import gift.Service.KakaoLoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/members")
@RestController
public class KakaoOauthController {

    private KakaoLoginService kakaoLoginService;

    public KakaoOauthController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/kakao-login")
    public RedirectView requestLogin(){
        URI redirectURI = kakaoLoginService.requestLogin();
        return new RedirectView(redirectURI.toString());
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String,String>> getToken(@RequestParam ("code") String oauthCode) {
        String accessToken = kakaoLoginService.getToken(oauthCode);
        Map<String, String> response = new HashMap<>();
        response.put("access_token", accessToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
