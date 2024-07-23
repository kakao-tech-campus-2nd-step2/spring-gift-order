package gift.user.controller;

import gift.user.client.KakaoLoginClient;
import gift.user.dto.request.UserLoginRequest;
import gift.user.dto.request.UserRegisterRequest;
import gift.user.dto.response.UserResponse;
import gift.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final KakaoLoginClient kakaoLoginClient;

    public UserController(UserService userService, KakaoLoginClient kakaoLoginClient) {
        this.userService = userService;
        this.kakaoLoginClient = kakaoLoginClient;
    }

    @PostMapping("register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid UserLoginRequest userRequest) {
        return ResponseEntity.ok(userService.loginUser(userRequest));
    }

    @RequestMapping("auth/kakao/code")
    public ResponseEntity<String> getCode(@RequestParam("code") String code) {
        var response = kakaoLoginClient.getKakaoTokenResponse(code);
        return null;
    }

}
