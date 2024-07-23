package gift.login.controller;

import gift.login.service.LoginService;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class LoginController {
    private final LoginService loginService;
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }
    @GetMapping("")
    public ResponseEntity<String> handleOAuthCallback(
        @RequestParam(value = "code") String code) {
        System.out.println("token: " + code);
        String accessToken = loginService.getAccessToken(code);
        System.out.println("access_token: " + accessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
