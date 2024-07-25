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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class KakaoLoginController {

    @Value("${kakao.client-id}")
    private String REST_API_KEY;

    @Value("${kakao.redirect-url}")
    private String REDIRECT_URI;

    private final KakaoLoginService kakaoLoginService;
    private final ProductService productService;
    private final UserService userService;
    private final JWTService jwtService;


    private static final Logger log = LoggerFactory.getLogger(KakaoLoginController.class);

    public KakaoLoginController(KakaoLoginService kakaoLoginService, ProductService productService, UserService userService, JWTService jwtService) {
        this.kakaoLoginService = kakaoLoginService;
        this.productService = productService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/login")
    public String getLoginView(Model model){
        model.addAttribute("redirect_url", REDIRECT_URI);
        model.addAttribute("client_id", REST_API_KEY);
        model.addAttribute("loginDTO", new LoginDTO());
        return "Login";
    }

    @ResponseBody
    @GetMapping("/")
    public Token getCode(@RequestParam String code){
        log.info("[code] : " + code);
        KakaoResponse response = kakaoLoginService.login(code);
        String accessToken = response.getAccess_token();
        log.info("[access toke] : " + accessToken);
        Long socialID = kakaoLoginService.getUserInfo(accessToken);

        Optional<KakaoUser> kakaoUser = userService.findByKakaoSocialID(kakaoLoginService.getUserInfo(accessToken));
        KakaoUserDTO kakaoUserDTO = new KakaoUserDTO(socialID, accessToken, response.refresh_token);
        Token token = new Token(jwtService.generateAccessToken(kakaoUserDTO));

        if(kakaoUser.isEmpty()){
            userService.registerKakaoUser(kakaoUserDTO);
            return token;
        }

        userService.updateKakaoUserToken(kakaoUserDTO);
        return token;
    }

}
