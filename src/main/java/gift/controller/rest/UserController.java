package gift.controller.rest;

import gift.entity.UserDTO;
import gift.service.UserService;
import gift.util.ResponseUtility;
import gift.util.UserUtility;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Map<String, String>> signup(@RequestBody @Valid UserDTO form, HttpSession session) {
        String accessToken = userService.signup(form);
        session.setAttribute("email", form.getEmail());
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("kakaoAccessToken", "");
        session.setAttribute("isSocialAccount", false);
        return ResponseEntity.ok().body(userUtility.accessTokenToObject(accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid UserDTO form, HttpSession session) {
        String accessToken = userService.login(form);
        session.setAttribute("email", form.getEmail());
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("kakaoAccessToken", "");
        session.setAttribute("isSocialAccount", false);
        return ResponseEntity.ok().body(userUtility.accessTokenToObject(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session, HttpServletRequest request) {
        session.invalidate();
        HttpSession newSession = request.getSession(true);
        return ResponseEntity.ok().body(responseUtility.makeResponse("logout successfully"));
    }

    @PostMapping("/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody Map<String, String> request, HttpSession session) {
        String code = request.get("code");

        Map<String, String> response = userService.kakaoLogin(code);

        session.setAttribute("email", response.get("email"));
        session.setAttribute("accessToken", response.get("accessToken"));
        session.setAttribute("kakaoAccessToken", response.get("kakaoAccessToken"));
        session.setAttribute("isSocialAccount", true);

        Map<String, String> data = new HashMap<>();
        return ResponseEntity
                .ok()
                .body(userUtility.accessTokenToObject(data.get("accessToken")));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            String accessToken = (String) session.getAttribute("accessToken");
            String kakaoAccessToken = (String) session.getAttribute("kakaoAccessToken");
            boolean isSocialAccount = (Boolean) session.getAttribute("isSocialAccount");

            Map<String, String> res = new HashMap<>();
            res.put("email", email);
            res.put("accessToken", accessToken);
            res.put("kakaoAccessToken", kakaoAccessToken);
            if (isSocialAccount) {
                res.put("isSocialAccount", "true");
            } else {
                res.put("isSocialAccount", "false");
            }
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            Map<String, String> res = new HashMap<>();
            res.put("msg", "로그인 안됨");
            return ResponseEntity.ok().body(res);
        }
    }
}
