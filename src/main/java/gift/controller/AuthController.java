package gift.controller;

import gift.auth.dto.KakaoAccessToken;
import gift.auth.dto.KakaoLoginAuthorizationCode;
import gift.auth.dto.KakaoProperties;
import gift.domain.User;
import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.UserLoginRequestDTO;
import gift.dto.requestdto.UserSignupRequestDTO;
import gift.dto.responsedto.UserResponseDTO;
import gift.service.AuthService;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessBody<UserResponseDTO>> signUp(
        @Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        userService.join(userSignupRequestDTO);
        UserResponseDTO userResponseDTO = authService.register(userSignupRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.CREATED, "회원가입에 성공했습니다.", userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessBody<UserResponseDTO>> login(
        @Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        User user = userService.findByEmail(userLoginRequestDTO);
        UserResponseDTO userResponseDTO = authService.login(user, userLoginRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.ACCEPTED, "로그인에 성공했습니다.", userResponseDTO);
    }

    @GetMapping("/redirect")
    public String redirect() {
        KakaoProperties properties = authService.getProperties();
        String clientId = properties.clientId();
        String redirectUri = properties.redirectUrl();
        return "redirect:https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=" + redirectUri +  "&client_id=" + clientId;
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<SuccessBody<String>> login(
        @RequestHeader KakaoLoginAuthorizationCode kakaoLoginAuthorizationCode) {
        String accessToken = authService.getAccessToken(kakaoLoginAuthorizationCode.code());
        return ApiResponseGenerator.success(HttpStatus.OK, "액세스 토큰 발급 성공", accessToken);
    }
}
