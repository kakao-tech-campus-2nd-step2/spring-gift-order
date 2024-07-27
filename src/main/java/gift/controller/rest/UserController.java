package gift.controller.rest;

import gift.entity.UserDTO;
import gift.service.UserService;
import gift.util.ResponseUtility;
import gift.util.UserUtility;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserUtility userUtility;
    private final UserService userService;
    private final ResponseUtility responseUtility;

    @Autowired
    public UserController(UserUtility userUtility, UserService userService, ResponseUtility responseUtility) {
        this.userUtility = userUtility;
        this.userService = userService;
        this.responseUtility = responseUtility;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody @Valid UserDTO userDTO, HttpSession session) {
        String accessToken = userService.signup(userDTO);
        session.setAttribute("email", userDTO.getEmail());
        session.setAttribute("role", "USER");
        return ResponseEntity.ok().body(userUtility.accessTokenToObject(accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserDTO form, HttpSession session) {
        String accessToken = userService.login(form);
        session.setAttribute("email", form.getEmail());
        session.setAttribute("role", userService.findOne(form.getEmail()).getRole());
        return ResponseEntity.ok().body(userUtility.accessTokenToObject(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(responseUtility.makeResponse("logout successfully"));
    }

    @PostMapping("/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");

        String kakaoAccessToken = userService.kakaoLogin(code);
        Map<String, String> response = userService.getKakaoProfile(kakaoAccessToken);

        session.setAttribute("email", response.get("email"));
        session.setAttribute("role", "USER");
        session.setAttribute("kakaoAccessToken", kakaoAccessToken);

        return ResponseEntity
                .ok()
                .body(userUtility.accessTokenToObject(response.get("accessToken")));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            String role = (String) session.getAttribute("role");

            Map<String, String> res = new HashMap<>();
            res.put("email", email);
            res.put("role", role);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            Map<String, String> res = new HashMap<>();
            res.put("msg", "로그인 안됨");
            return ResponseEntity.ok().body(res);
        }
    }
}
