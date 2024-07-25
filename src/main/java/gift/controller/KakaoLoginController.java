package gift.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login/kakao")
public class KakaoLoginController {
    @GetMapping
    public void getCode(HttpServletResponse response){
    }
}
