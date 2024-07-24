package gift.product.controller.auth;

import gift.product.dto.auth.JwtResponse;
import gift.product.dto.auth.MemberDto;
import gift.product.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final String REDIRECT_ADMIN_LOGIN_PROCESSING = "redirect:/admin/login/process";
    private final AuthService authService;

    public AdminAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admin/loginForm";
    }

    @PostMapping("/login")
    public String login(MemberDto memberDto, RedirectAttributes redirectAttributes) {
        JwtResponse jwtResponse = authService.login(memberDto);
        redirectAttributes.addAttribute("accessToken", jwtResponse.token());

        return REDIRECT_ADMIN_LOGIN_PROCESSING;
    }

    @GetMapping("/login/process")
    public String loginProcess(@RequestParam("accessToken") String accessToken, Model model) {
        model.addAttribute("accessToken", accessToken);

        return "admin/loginProcess";
    }

    @PostMapping("login/process")
    @ResponseBody
    public ResponseEntity<Void> loginSuccess(@RequestParam("accessToken") JwtResponse jwtResponse,
        HttpServletResponse response) {
        addAccessTokenCookieInResponse(jwtResponse, response);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/admin/products"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private void addAccessTokenCookieInResponse(JwtResponse jwtResponse,
        HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", jwtResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
