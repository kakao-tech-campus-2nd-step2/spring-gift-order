package gift.controller;

import gift.config.KakaoProperties;
import gift.service.KakaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping
    public RedirectView kakaoLogin() {
        return new RedirectView(
                UriComponentsBuilder.newInstance()
                        .scheme("https")
                        .host("kauth.kakao.com")
                        .path("/oauth/authorize")
                        .queryParam("client_id", kakaoProperties.clientId())
                        .queryParam("redirect_uri", kakaoProperties.redirectURL())
                        .queryParam("response_type", "code")
                        .build(true).toString()
        );
    }

    @GetMapping("/redirect")
    public void getToken(
            @RequestParam(value = "code") String kakaoCode
    ) {
        kakaoService.getKakaoToken(kakaoCode);
        //TODO 토큰 파싱해서 응답
    }
}
