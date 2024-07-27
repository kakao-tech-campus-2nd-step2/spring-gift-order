package gift.Controller;

import gift.DTO.ResponseLoginDTO;
import gift.Service.KakaoLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

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
    public ResponseLoginDTO loginOrRegisterUser(@RequestParam ("code") String oauthCode) {
            return new ResponseLoginDTO(kakaoLoginService.loginOrRegisterUser(oauthCode));
    }
}
