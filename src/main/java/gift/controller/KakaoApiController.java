package gift.controller;

import gift.service.KakaoApiService;
import gift.service.UserService;
import gift.dto.OrderRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kakao")
public class KakaoApiController {
    private final KakaoApiService kakaoApiService;
    private final UserService userService;

    private String accessToken;

    public KakaoApiController(KakaoApiService kakaoApiService, UserService userService) {
        this.kakaoApiService = kakaoApiService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String kakaoLogin() {
        return "redirect:" + kakaoApiService.kakaoLogin();
    }

    @GetMapping
    public String kakaoLoginVerify(@RequestParam("code") String authorizationCode) {
        accessToken = kakaoApiService.getAccessToken(authorizationCode);
        String email = kakaoApiService.getEmailFromToken(accessToken);
        userService.kakaoSign(email);

        return "redirect:/kakao/login/success";
    }

    @GetMapping("login/success")
    public String kakaoLoginSuccess(Model model) {
        model.addAttribute("accessToken", accessToken);

        return "kakaoLogin";
    }

    @PostMapping("/send/message")
    public ResponseEntity<Void> sendMessage(String accessToken, @RequestBody OrderRequestDTO orderRequestDTO) {
        kakaoApiService.sendMessage(accessToken, orderRequestDTO);
        return ResponseEntity.ok().build();
    }
}
