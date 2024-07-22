package gift.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ConfigurationProperties(prefix = "kakao")
public class KakaoLoginApiController {

    private final RestClient client = RestClient.builder().build();

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

    @RequestMapping("/logined")
    public String logined(@RequestParam("body") String body, Model model) {
        model.addAttribute("body", body);
        return "successKakaoLogin";
    }

    @RequestMapping("/kakao/login/oauth2/code")
    public String getToken(@RequestParam("code") String code, RedirectAttributes re) {

        String url = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        ResponseEntity<String> response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
        re.addAttribute("body", response.getBody());
        return "redirect:/logined";
    }




}
