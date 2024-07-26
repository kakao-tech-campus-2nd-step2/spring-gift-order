package gift.service;

import org.springframework.stereotype.Service;

import gift.entity.User;
import gift.exception.UnauthorizedException;
import gift.repository.UserRepository;

@Service
public class UserService {

    private final AuthService authService;
    private final KakaoAuthService kakaoAuthService;
    private final UserRepository userRepository;

    public UserService(AuthService authService, KakaoAuthService kakaoAuthService, UserRepository userRepository) {
        this.authService = authService;
        this.kakaoAuthService = kakaoAuthService;
        this.userRepository = userRepository;
    }

    public User getUserFromToken(String token) {
        String email;
        try {
            email = authService.parseToken(token);
        } catch (UnauthorizedException e) {
            email = kakaoAuthService.parseKakaoToken(token);
        }
        return findOrRegisterUser(email);
    }

    private User findOrRegisterUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(email, "default_password123");
            userRepository.save(newUser);
            return newUser;
        });
    }
}
