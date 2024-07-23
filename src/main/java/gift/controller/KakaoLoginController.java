package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.KakaoProperties;
import gift.DTO.Token;
import gift.service.KakaoUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KakaoLoginController {
    private final KakaoUserService kakaoUserService;

    private final KakaoProperties kakaoProperties;

    public KakaoLoginController(KakaoUserService kakaoUserService, KakaoProperties kakaoProperties) {
        this.kakaoUserService = kakaoUserService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/")
    public RedirectView kakaoLogin(
            @RequestParam(required = false) String code, RedirectAttributes redirectAttributes
    ) throws JsonProcessingException {
        if(code != null){
            Token login = kakaoUserService.login(code);
            redirectAttributes.addFlashAttribute("token", login.getToken());
            return new RedirectView("/home");
        }
        return new RedirectView("/home");
    }

    @GetMapping("/kakaoLogin")
    public String OauthLogin(){
        String url = "https://kauth.kakao.com/oauth/authorize?";
        url += "scope=talk_message&";
        url += "response_type=code&";
        url += "redirect_uri=" + kakaoProperties.getRedirectUrl() +"&";
        url += "client_id=" + kakaoProperties.getClientId();
        return "redirect:" + url;
    }
}
