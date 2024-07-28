package gift.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.entity.User;
import gift.service.AuthService;
import gift.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/members")
public class AuthController {
	
	private final AuthService authService;
	private final UserService userService;
	
	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody User user, BindingResult bindingResult){
		userService.createUser(user, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody User user, BindingResult bindingResult){
		Map<String, String> accessToken = authService.loginUser(user, bindingResult);
        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }
}
