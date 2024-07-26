package gift.Controller;

import gift.Model.DTO.MemberDTO;
import gift.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class LoginRestController {
    private final UserService userService;

    public LoginRestController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/members/register")
    public ResponseEntity<?> register(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok(userService.register(memberDTO));
    }

    @PostMapping("/members/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok(userService.login(memberDTO));
    }

    @GetMapping("/kakao/link")
    public void test(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", userService.getKakaoLoginUrl());
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        ResponseEntity<?> result = userService.kakaoLogin(code);
        return result;
    }
}
