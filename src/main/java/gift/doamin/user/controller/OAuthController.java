package gift.doamin.user.controller;

import gift.doamin.user.properties.KakaoClientProperties;
import gift.doamin.user.properties.KakaoProviderProperties;
import gift.doamin.user.util.AuthorizationOAuthUriBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class OAuthController {

    private final KakaoClientProperties clientProperties;
    private final KakaoProviderProperties providerProperties;

    public OAuthController(KakaoClientProperties clientProperties,
        KakaoProviderProperties providerProperties) {
        this.clientProperties = clientProperties;
        this.providerProperties = providerProperties;
    }

    @GetMapping("/oauth2/authorization/kakao")
    public ModelAndView kakaoLogin(HttpServletResponse response) {
        String url = new AuthorizationOAuthUriBuilder()
            .clientProperties(clientProperties)
            .providerProperties(providerProperties)
            .build();

        return new ModelAndView("redirect:" + url);
    }

    @GetMapping("login/oauth2/code/kakao")
    public ResponseEntity<Void> kakaoLogin(@RequestParam(name = "code") String authorizeCode) {
        System.out.println("authorizeCode = " + authorizeCode);
        return null;
    }

}
