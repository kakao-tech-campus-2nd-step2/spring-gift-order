package gift.domain.user;

import gift.domain.user.dto.kakao.KaKaoToken;
import gift.domain.user.dto.UserDTO;
import gift.domain.user.dto.UserInfo;
import gift.global.exception.ErrorCode;
import gift.global.jwt.JwtProvider;
import gift.global.resolver.LoginInfo;
import gift.global.response.ResponseMaker;
import gift.global.response.SimpleResultResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public UserRestController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 회원 가입
     */
    @PostMapping
    public ResponseEntity<SimpleResultResponseDto> join(@Valid @RequestBody UserDTO userDTO) {
        userService.join(userDTO);

        return ResponseMaker.createSimpleResponse(HttpStatus.OK, "회원 가입에 성공했습니다");
    }

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<SimpleResultResponseDto> login(@Valid @RequestBody UserDTO userDTO) {
        String jwt = userService.login(userDTO);

        return ResponseMaker.createSimpleResponseWithJwtOnHeader(HttpStatus.OK, "로그인에 성공했습니다", jwt);
    }

    /**
     * 카카오 회원 로그인
     */
    @GetMapping("kakaoLogin")
    public ResponseEntity<SimpleResultResponseDto> kakaoLogin(
        @RequestParam(value = "code", required = false) String authorizedCode, // 없으면 null
        @RequestParam(value = "error", required = false) String error // 없으면 null
    ) {
        if (authorizedCode != null) {
            KaKaoToken kaKaoToken = userService.getToken(authorizedCode);

            User findUser = userService.findUserByKakaoAccessToken(
                kaKaoToken.accessToken());

            String jwt = JwtProvider.generateToken(findUser);
            System.out.println("jwt = " + jwt);

            // TODO 커스텀 헤더 포함 응답하는 메서드만들어서 홈 화면으로 리다이렉트 처리 필요
            return ResponseMaker.createSimpleResponseWithJwtOnHeader(HttpStatus.OK, "로그인에 성공했습니다.", jwt);

        }
        // TODO 커스텀 헤더 포함 응답하는 메서드만들어서 홈 화면으로 리다이렉트 처리필요
        return ResponseMaker.createSimpleResponse(HttpStatus.OK, "로그인 과정이 취소되었습니다.");
    }

    @GetMapping("kakaoLogin/refresh")
    public void kakaoLoginRefresh(@LoginInfo UserInfo userInfo) {
    }
}
