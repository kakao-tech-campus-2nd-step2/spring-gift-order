package gift.controller;

import static gift.service.KakaoLoginService.TOKEN_REQUEST_URI;

import gift.service.KakaoLoginService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

@Controller
@ConfigurationProperties(prefix = "kakao")
public class KakaoLoginApiController {

    private final KakaoLoginService kakaoLoginService;
    private final RestClient client = RestClient.builder().build();

    @Autowired
    public KakaoLoginApiController(KakaoLoginService kakaoLoginService) {
        this.kakaoLoginService = kakaoLoginService;
    }

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/kakao/login")
    public String login(Model model) {
        model.addAttribute("client_id", clientId);
        model.addAttribute("redirect_uri", redirectUri);
        return "kakaoLogin";
    }

    @RequestMapping("/kakao/login/oauth2/code")
    @ResponseBody
    public ResponseEntity<String> getToken(@RequestParam("code") String code) {
        ResponseEntity<String> tokenResponse = client.post()
            .uri(URI.create(TOKEN_REQUEST_URI))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(kakaoLoginService.createTokenRequest(clientId, redirectUri, code))
            .retrieve()
            .toEntity(String.class);
        return new ResponseEntity<>(tokenResponse.getBody(), HttpStatus.OK);
    }


}
