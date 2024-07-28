package gift.controller.rest;

import gift.entity.AccessTokenResponseDTO;
import gift.entity.MessageResponseDTO;
import gift.entity.UserDTO;
import gift.entity.UserResponseDTO;
import gift.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AccessTokenResponseDTO> signup(@RequestBody @Valid UserDTO userDTO, HttpSession session) {
        String accessToken = userService.signup(userDTO);
        session.setAttribute("email", userDTO.getEmail());
        session.setAttribute("role", "USER");
        return ResponseEntity.ok().body(makeAccessTokenResponse(accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponseDTO> login(@RequestBody @Valid UserDTO form, HttpSession session) {
        String accessToken = userService.login(form);
        session.setAttribute("email", form.getEmail());
        session.setAttribute("role", userService.findOne(form.getEmail()).getRole());
        return ResponseEntity.ok().body(makeAccessTokenResponse(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(new MessageResponseDTO("Logged out successfully"));
    }

    @PostMapping("/kakao")
    public ResponseEntity<AccessTokenResponseDTO> kakaoLogin(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");

        String kakaoAccessToken = userService.kakaoLogin(code);
        Map<String, String> response = userService.getKakaoProfile(kakaoAccessToken);

        session.setAttribute("email", response.get("email"));
        session.setAttribute("role", "USER");
        session.setAttribute("kakaoAccessToken", kakaoAccessToken);

        return ResponseEntity.ok().body(makeAccessTokenResponse(response.get("accessToken")));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(HttpSession session) {
        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");

        UserResponseDTO res = new UserResponseDTO(email, role);
        return ResponseEntity.ok().body(res);
    }

    private AccessTokenResponseDTO makeAccessTokenResponse(String accessToken) {
        return new AccessTokenResponseDTO(accessToken);
    }
}
