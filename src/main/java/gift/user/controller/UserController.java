package gift.user.controller;

import gift.user.dto.request.UserLoginRequest;
import gift.user.dto.request.UserRegisterRequest;
import gift.user.dto.response.UserResponse;
import gift.user.service.KakaoUserService;
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
    private final KakaoUserService kakaoUserService;

    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
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
    public ResponseEntity<UserResponse> getKakaoUserId(@RequestParam("code") String code) {
        var response = kakaoUserService.loginKakaoUser(code);
        return ResponseEntity.ok(response);
    }

}
