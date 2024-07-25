package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.*;
import gift.main.entity.User;
import gift.main.repository.UserRepository;
import gift.main.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public String joinUser(UserJoinRequest userJoinRequest) {
        if (userRepository.existsByEmail(userJoinRequest.email())) {
            throw new CustomException(ErrorCode.ALREADY_EMAIL);
        }
        User userdto = new User(userJoinRequest);
        User user = userRepository.save(userdto);
        return jwtUtil.createToken(user);

    }

    public String loginUser(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmailAndPassword(userLoginRequest.email(), userLoginRequest.password())
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_LOGIN));
        return jwtUtil.createToken(user);
    }

    @Transactional
    public Map<String, Object> loginKakaoUser(User user) {
        User saveUser = userRepository.findByEmail(user.getEmail())
                .orElseGet(() -> userRepository.save(user));
        String token = jwtUtil.createToken(saveUser);
        Map<String, Object> responseMap = new HashMap<>();

        // token과 userVo를 Map에 추가
        responseMap.put("token", token);
        responseMap.put("user", saveUser);

        // Map 반환
        return responseMap;

    }
}
