package gift.domain.user.controller;

import gift.auth.jwt.JwtToken;
import gift.domain.user.dto.UserRequest;
import gift.domain.user.dto.UserLoginRequest;
import gift.domain.user.service.UserService;
import gift.exception.DuplicateEmailException;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtToken> create(@RequestBody @Valid UserRequest userRequest) {
        try {
            JwtToken jwtToken = userService.signUp(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException("error.duplicate.key.email");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        JwtToken jwtToken = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
