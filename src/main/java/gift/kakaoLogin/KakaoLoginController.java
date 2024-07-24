package gift.kakaoLogin;

import gift.jwt.JWTService;
import gift.product.ProductService;
import gift.user.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String REST_API_KEY;

    @Value("${kakao.redirect-url}")
    private String REDIRECT_URI;

    private final KakaoLoginService kakaoLoginService;
    private final ProductService productService;


    private static final Logger log = LoggerFactory.getLogger(KakaoLoginController.class);

    public KakaoLoginController(KakaoLoginService kakaoLoginService, ProductService productService) {
        this.kakaoLoginService = kakaoLoginService;
        this.productService = productService;
    }

    @GetMapping("/login")
    public String getLoginView(Model model){
        model.addAttribute("redirect_url", REDIRECT_URI);
        model.addAttribute("client_id", REST_API_KEY);
        model.addAttribute("loginDTO", new LoginDTO());
        return "Login";
    }

    @GetMapping("/")
    public String getCode(@RequestParam String code, Model model){
        log.info("[code] : " + code);
        String accessToken = kakaoLoginService.login(code);
        log.info("[access toke] : " + accessToken);
        model.addAttribute("products", productService.findAllProducts());
        return "MainView";
    }

}
