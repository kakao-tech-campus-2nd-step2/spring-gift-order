package gift.controller.api;

import gift.dto.response.KakaoTokenResponse;
import gift.service.KakaoLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {

    KakaoLoginService kakaoLoginService;

    public KakaoLoginController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    //카카오 로그인 성공후에 내서버에서 인가 코드 요청하는것임
    @GetMapping("/kakaologin")
    public void kakaoLogin() {
        kakaoLoginService.requestAuthorizationCode();
    }

    @GetMapping("/")
    public String getToken(@RequestParam("code") String code) {
        //토큰 얻기
        KakaoTokenResponse token = kakaoLoginService.getToken(code);

        //사용자 로그인 처리  토큰에 카카오의 회원번오있음. 이거 우리 멤버 디이에 있는지 확인
        //kakaoLoginService.verifyMember(token);

        System.out.println(token);
        return "성공함";
    }
}
