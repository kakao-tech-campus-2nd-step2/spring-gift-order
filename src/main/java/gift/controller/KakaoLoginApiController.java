package gift.controller;

import static gift.service.KakaoLoginService.TOKEN_REQUEST_URI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.kakao.KakaoAuthException;
import gift.exception.kakao.KakaoTokenException;
import gift.response.KakaoTokenResponse;
import gift.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Controller
@ConfigurationProperties(prefix = "kakao")
public class KakaoLoginApiController {

    private final KakaoLoginService kakaoLoginService;

    @Autowired
    public KakaoLoginApiController(KakaoLoginService kakaoLoginService, ObjectMapper mapper) {
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
    public ResponseEntity<KakaoTokenResponse> getToken(
        @RequestParam MultiValueMap<String, Object> params) throws JsonProcessingException {

        kakaoLoginService.checkRedirectUriParams(params);
        try {
            String code = params.get("code").toString();
            KakaoTokenResponse dto = kakaoLoginService.getToken(clientId, redirectUri, code);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            throw new KakaoTokenException(e);
        }
    }


}
