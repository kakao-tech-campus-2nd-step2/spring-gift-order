package gift.auth;

import gift.entity.UserEntity;
import gift.repository.UserRepository;
import gift.util.PasswordCrypto;
import gift.util.errorException.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;
    private final PasswordCrypto passwordCrypto;

    @Autowired
    public LoginService(UserRepository userRepository, JwtToken jwtToken,
        PasswordCrypto passwordCrypto) {
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
        this.passwordCrypto = passwordCrypto;
    }

    public Token Login(Login login) {
        UserEntity user = userRepository.findByEmailAndIsDelete(login.getEmail(), 0).orElseThrow(
            () -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );

        if (!passwordCrypto.matchesPassword(login.getPassword(), user.getPassword())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        return jwtToken.createToken(user);
    }
}
