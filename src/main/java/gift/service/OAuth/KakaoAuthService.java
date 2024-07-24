package gift.service.OAuth;

import gift.config.KakaoProperties;
import gift.model.user.User;
import gift.repository.user.UserRepository;
import gift.util.AuthUtil;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {

    private final AuthUtil authUtil;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public KakaoAuthService(AuthUtil authUtil, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authUtil = authUtil;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String createCodeUrl(){
        return authUtil.createGetCodeUrl();
    }
    public String getAccessToken(String authCode) {
        return authUtil.getAccessToken(authCode);
    }


    public String register(String accessToken){
        String email = authUtil.extractUserEmail(accessToken);

        User user = userRepository.findByEmail(email).orElseGet(
                ()->userRepository.save(new User(email,"1234"))
        );

        return jwtUtil.generateJWT(user);
    }
}
