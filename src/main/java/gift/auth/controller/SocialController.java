package gift.auth.controller;

import gift.auth.domain.KakaoToken.kakaoToken;
import gift.auth.service.SocialService;
import gift.util.page.SingleResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final SocialService socialService;

    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    @GetMapping("/token/kakao")
    public SingleResult<kakaoToken> GetKakaoToken(@Valid @RequestParam String code) {
        return new SingleResult<>(socialService.getKakaoToken(code));
    }

    //    타인을 강제로 할 우려가 있어 토큰내 값으로 유저 사용
//    로그인 상태에서 가능
    @PostMapping("/kakao")
    public SingleResult<Long> SetToKakao(HttpServletRequest req, @RequestBody kakaoToken token) {
        return new SingleResult<>(socialService.SetToKakao(req, token));
    }
}
