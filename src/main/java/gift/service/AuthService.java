package gift.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import gift.entity.User;
import gift.exception.InvalidUserException;
import gift.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService implements TokenHandler{
	
	private final UserService userService;
	
	public AuthService(UserService userService) {
		this.userService = userService;
	}
	
	private final String secret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
	
	public User searchUser(String email, BindingResult bindingResult) {
		validateBindingResult(bindingResult);       
        return userService.findByEmail(email);
	}
	
	public Map<String, String> loginUser(User user, BindingResult bindingResult){
		User registeredUser = searchUser(user.getEmail(), bindingResult);
		registeredUser.validatePassword(user.getPassword());
		String token = grantAccessToken(registeredUser);
		return loginResponse(token);
	}
	
	public String grantAccessToken(User user) {
		return Jwts.builder()
		    .setSubject(user.getId().toString())
		    .claim("userEmail", user.getEmail())
		    .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
		    .compact();
	}
	
	@Override
	public String parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token.replace("Bearer ", ""))
                    .getPayload()
                    .get("userEmail", String.class);
        } catch(Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
	
	@Override
	public String getTokenSuffix() {
		return "-itself";
	}
	
	private void validateBindingResult(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errorMessage = bindingResult
					.getFieldError()
					.getDefaultMessage();
			throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}
	
	private Map<String, String> loginResponse(String token){
		Map<String, String> response = new HashMap<>();
		response.put("access_token", token);
		return response;
	}
}
