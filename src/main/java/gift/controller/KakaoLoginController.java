package gift.controller;


import gift.service.KakaoLoginService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Controller
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService ;

    @Autowired
    public KakaoLoginController(KakaoLoginService kakaoLoginService){
        this.kakaoLoginService = kakaoLoginService ;
    }


    // 실행되면
    @GetMapping("/kakao-login")
    public String kakaoLogin() {
        String authorizationURI = kakaoLoginService.makeKakaoAuthorizationURI();
        return "redirect:" + authorizationURI;
    }
    
    // kakaoLogin 다 하면 이 쪽으로 옴
    // code 받았으니 kakao 서버로 token post 요청해야 함
    // code 추출하고 URI 만든 다음
    // 그 코드를 redirect 해야 함.
    @GetMapping("/")
    public ResponseEntity<String> getKakaoToken(@RequestParam(name = "code") String code) {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<LinkedMultiValueMap<String, String>> request = kakaoLoginService.getKakaoAuthorizationToken(code);
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        return response;
    }





}
