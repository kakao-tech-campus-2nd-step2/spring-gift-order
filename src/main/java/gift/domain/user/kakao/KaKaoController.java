package gift.domain.user.kakao;

import gift.domain.user.User;
import gift.global.jwt.JwtProvider;
import gift.global.response.ResponseMaker;
import gift.global.response.SimpleResultResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/users/oauth")
public class KaKaoController {
    private final KaKaoService kaKaoService;

    public KaKaoController(KaKaoService kaKaoService) {
        this.kaKaoService = kaKaoService;
    }

        /**
         * 카카오 로그인 페이지로 이동
         */
        @GetMapping("/kakao/login")
        public RedirectView LoginPage(){
            return new RedirectView(kaKaoService.buildLoginPageUrl());
        }

        /**
         * 카카오 로그인 인가코드로 JWT 발급
     */
    @GetMapping("/kakao")
    public ResponseEntity<SimpleResultResponseDto> JwtToken(
        @RequestParam(value = "code", required = false) String authorizedCode
    ) {
        KaKaoToken kaKaoToken = kaKaoService.getKaKaoToken(authorizedCode);
        System.out.println("kaKaoToken = " + kaKaoToken);

        User findUser = kaKaoService.findUserByKaKaoAccessToken(kaKaoToken.accessToken());

        String jwt = JwtProvider.generateToken(findUser);
        return ResponseMaker.createSimpleResponseWithJwtOnHeader(HttpStatus.OK, "카카오 로그인 성공", jwt);
    }
}
