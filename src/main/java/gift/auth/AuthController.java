package gift.auth;

import gift.exception.NotFoundMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(@RequestHeader("Authorization") String authHeader, @RequestBody LoginRequestDto loginRequestDto)
        throws NotFoundMember {
        return authService.login(authHeader,loginRequestDto);
    }

    @GetMapping("/token")
    public String getToken(@RequestParam("code") String authorizationCode) {
        return authService.getTokenFromAuthorizationCode(authorizationCode);
    }

}
