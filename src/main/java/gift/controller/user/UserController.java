package gift.controller.user;

import gift.dto.user.UserRequest;
import gift.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "유저 등록 및 로그인", description = "유저 등록 및 로그인을 위한 API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인", description = "유저 인증을 진행하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\"access_token\":\"exampleToken\"}")
                            )
                    )
            }
    )
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest.Check userRequest) {
        String token = userService.login(userRequest);
        return ResponseEntity.ok(Map.of("accessToken", token));
    }

    @PostMapping("/register")
    @Operation(
            summary = "유저 회원가입",
            description = "새로운 유저를 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공")
            }
    )
    public ResponseEntity<String> register(@RequestBody UserRequest.Create userRequest) {
        userService.register(userRequest);
        return ResponseEntity.ok("회원가입을 성공하였습니다!");
    }
}