package gift.controller;

import gift.dto.KakaoInfoDto;
import gift.model.member.KakaoProperties;
import gift.model.member.Member;
import gift.service.KakaoService;
import gift.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService,MemberService memberService){
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/login")
    public String kakaoLogin() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoProperties.getClientId());
        url.append("&redirect_uri="+kakaoProperties.getRedirectUri());
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code, HttpSession session) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoInfoDto kakaoInfoDto = kakaoService.getUserInfo(accessToken);
        String email = kakaoInfoDto.kakaoAccount().email();
        Member kakaoMember = kakaoService.registerOrGetKakaoMember(email);

        session.setAttribute("loginMember", kakaoMember);
        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("kakaoToken", accessToken);

        return ResponseEntity.ok("Access Token: " + accessToken);
    }

    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)) {
            kakaoService.kakaoDisconnect(accessToken);
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
        }
        return "redirect:/";
    }
}
