package gift.domain.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.user.dto.kakao.KaKaoToken;
import gift.domain.user.dto.UserDTO;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.user.UserDuplicateException;
import gift.global.jwt.JwtProvider;
import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${kakao.client.id}")
    private String clientId; // 카카오 개발자 콘솔에서 발급받은 클라이언트 ID

    @Value("${kakao.redirect.url}")
    private String redirectUri; // 카카오 로그인 후 리다이렉트될 URI
    private final JpaUserRepository userRepository;

    public UserService(JpaUserRepository jpaUserRepository) {
        this.userRepository = jpaUserRepository;
    }

    /**
     * 회원 가입
     */
    public void join(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserDuplicateException(userDTO.getEmail());
        }

        User user = userDTO.toUser();
        userRepository.save(user);

    }


    /**
     * 로그인, 성공 시 JWT 반환
     */
    public String login(UserDTO userDTO) {
        User user = userRepository.findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword())
            .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "입력 정보가 올바르지 않습니다."));

        // jwt 토큰 생성
        String jwt = JwtProvider.generateToken(user);

        return jwt;
    }

    /**
     * 카카오 로그인
     * 인가 코드에서 엑세스 토큰 추출
     */
    public KaKaoToken getToken(String authorizedCode) {
        var url = "https://kauth.kakao.com/oauth/token";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizedCode);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println("response.getBody() = " + response.getBody());
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                String accessToken = rootNode.path("access_token").asText();
                String refreshToken = rootNode.path("refresh_token").asText();

                return new KaKaoToken(accessToken, refreshToken);

            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "토큰을 처리하는 중 오류가 발생했습니다.");
            }
        } else {
            System.out.println("응답 실패: " + response.getStatusCode());
            throw new BusinessException(ErrorCode.FORBIDDEN, "사용자 권한이 없습니다.");
        }
    }

    public User findUserByKakaoAccessToken(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request,
            String.class);

        User findUser = null;

        if(response.getStatusCode() == HttpStatus.OK){
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // 사용자 정보 추출
                String id = rootNode.path("id").asText(); // unique
                String nickname = rootNode.path("properties").path("nickname").asText();
                String email = rootNode.path("kakao_account").path("email").asText();

                System.out.println("id = " + id);
                System.out.println("nickname = " + nickname);
                System.out.println("email = " + email);

                findUser = userRepository.findByEmail(email);

                if(findUser == null){
                    String salt = UUID.randomUUID().toString();
                    String ori_password = salt + UUID.randomUUID().toString();
                    String hashed_password = ori_password; // hash 과정 생략

                    User user = new User(email, hashed_password);
                    userRepository.save(user);
                    findUser = userRepository.findByEmail(email);
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return findUser;
    }
}
