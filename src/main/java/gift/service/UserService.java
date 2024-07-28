package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import gift.entity.User;
import gift.exception.InvalidUserException;
import gift.exception.UserNotFoundException;
import gift.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final TokenService tokenService; 
	
	public UserService(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
	    		.orElseThrow(() -> new UserNotFoundException("email not found"));
	}
	
	public void createUser(User user, BindingResult bindingResult) {
		validateBindingResult(bindingResult);
		userRepository.save(user);
	}
	
	public User getUserFromToken(String token) {
    	String email = tokenService.extractionEmail(token);
    	return findByEmail(email);
    }
	
	private void validateBindingResult(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errorMessage = bindingResult
					.getFieldError()
					.getDefaultMessage();
			throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
		}
	}
}
