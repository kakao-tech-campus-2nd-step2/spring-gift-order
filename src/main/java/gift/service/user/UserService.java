package gift.service.user;


import gift.domain.user.User;
import gift.exception.user.InvalidCredentialsException;
import gift.exception.user.UserAlreadyExistsException;
import gift.exception.user.UserNotFoundException;
import gift.repository.user.UserRepository;
import gift.util.JwtTokenUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) throws UserNotFoundException, InvalidCredentialsException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return jwtTokenUtil.generateAccessToken(email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateRefreshToken(String email) {
        return jwtTokenUtil.generateRefreshToken(email);
    }

    public void blacklistToken(String token) {
        jwtTokenUtil.blacklistToken(token);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public void registerUser(String email, String password) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User already exists");
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(email, hashedPassword);
        userRepository.save(newUser);
    }

    //카카오 로그인

    //실제 경우 id 와 이메일을 둘다 받을수 있는 경우
    /*
    public User findOrCreateUser(Long id, String email) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            String randomPassword = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(randomPassword);
            User newUser = new User(id, email, encodedPassword);
            return userRepository.save(newUser);
        }
    }

     */

    public User findOrCreateUser(Long id, String email) {
        // 이메일이 주어진 경우, 해당 이메일로 사용자 검색
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            // 이메일로 사용자 찾음
            return userOptional.get();
        } else {
            // 이메일이 없거나 사용자 존재하지 않는 경우
            // id를 이메일로 사용하여 새로운 사용자 생성
            String randomPassword = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(randomPassword);

            // 이메일이 null 또는 빈 문자열인 경우, id를 문자열로 변환하여 이메일로 사용
            String emailToUse = (email == null || email.isEmpty()) ? id.toString() : email;

            // 중복 이메일 확인
            if (userRepository.findByEmail(emailToUse).isEmpty()) {
                User newUser = new User(id, emailToUse, encodedPassword);
                //System.out.println("Created new user: " + newUser);
                return userRepository.save(newUser);
            } else {
                // 중복 이메일 처리 로직 (예: 예외를 던지거나 다른 처리)
                //System.out.println("User already exists");
                //System.out.println(userRepository.findByEmail(emailToUse).get().getEmail());
                throw new RuntimeException("User with email already exists: " + emailToUse);
            }
        }
    }

}