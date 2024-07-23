package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gift.dto.KakaoProperties;
import gift.dto.response.KakaoTokenResponse;
import gift.service.KakaoApiService;

@Controller
public class KakaoLoginController {

    private KakaoProperties kakaoProperties;
    private KakaoApiService kakaoApiService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoApiService kakaoApiService){
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("kakaoApiKey", kakaoProperties.getApiKey());
        model.addAttribute("redirectUri", kakaoProperties.getRedirectUri());
        return "login";
    }

    @RequestMapping("/login/code")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {

        KakaoTokenResponse response = kakaoApiService.getToken(code);
        
        return new ResponseEntity<>(response.getAccessToken(), HttpStatus.OK);
    }
    
}
