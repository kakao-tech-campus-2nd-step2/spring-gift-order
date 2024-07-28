package gift.controller;

import gift.exception.NoSuchMemberException;
import gift.service.KakaoLoginService;
import gift.util.KakaoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoLoginService kakaoLoginService;

    @Autowired
    public LoginController(KakaoProperties kakaoProperties, KakaoLoginService kakaoLoginService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.clientId());
        model.addAttribute("kakaoRedirectUri", kakaoProperties.redirectUri());
        return "login";
    }

    @RequestMapping("/oauth2/kakao")
    public Object KakaoLogin(@RequestParam("code") String code) {
        try {
            return ResponseEntity.ok().body(kakaoLoginService.login(code));
        } catch (NoSuchMemberException e) {
            return "register";
        }
    }
}
