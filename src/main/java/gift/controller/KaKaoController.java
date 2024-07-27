package gift.controller;

import gift.classes.RequestState.RequestStatus;
import gift.classes.RequestState.SecureRequestStateDTO;
import gift.services.KaKaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("kakao")
public class KaKaoController {
    private final KaKaoService kaKaoService;

    public KaKaoController(KaKaoService kaKaoService) {
        this.kaKaoService = kaKaoService;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("kakaoUrl", kaKaoService.getKaKaoLogin());

        return "kakaologin";
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(HttpServletRequest request) throws Exception {
        String token = kaKaoService.getKaKaoToken(request.getParameter("code"));

        return ResponseEntity.ok().body(new SecureRequestStateDTO(
            RequestStatus.success,
            null,
            token
        ));
    }

}
