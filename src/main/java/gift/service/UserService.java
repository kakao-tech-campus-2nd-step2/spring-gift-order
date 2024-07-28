package gift.service;

import gift.dto.TokenResponse;
import gift.model.User;
import gift.repository.UserRepository;
import gift.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User loadOneUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User loginOrRegisterUser(String email, String token) {
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(email, UUID.randomUUID().toString());
            newUser.setKakaoAccessToken(token);
            return userRepository.save(newUser);
        });
        user.setKakaoAccessToken(token);
        return userRepository.save(user);
    }

    public TokenResponse createTokenResponse(User user) {
        String token = jwtTokenProvider.createToken(user.getEmail());
        return new TokenResponse(token);
    }
}