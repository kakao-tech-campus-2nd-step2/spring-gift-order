package gift.controller;

import gift.exception.BadRequestExceptions.BadRequestException;
import gift.service.KakaoAuthService;
import gift.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth/kakao")
public class kakaoAuthController {
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(kakaoAuthController.class);

    @Autowired
    kakaoAuthController(KakaoAuthService kakaoAuthService, JwtUtil jwtUtil) {
        this.kakaoAuthService = kakaoAuthService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public String logister(@RequestParam String code, Model model) {
        try {
            String token = jwtUtil.generateToken(kakaoAuthService.logister(code));
            model.addAttribute("token", token);
            model.addAttribute("success", true);
        } catch (BadRequestException e){
            model.addAttribute("message", e.getMessage());
            model.addAttribute("success", false);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            model.addAttribute("message", e.getMessage());
            model.addAttribute("success", false);
        }
        return "loginResult";
    }
}
