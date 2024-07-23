package gift.controller;

import gift.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/code")
    public ResponseEntity<String> LoginAndGetAccessToken(String code) {
        String accessToken = loginService.getAccessTokenByCode(code);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

}
