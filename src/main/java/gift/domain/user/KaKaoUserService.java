package gift.domain.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.user.dto.kakao.KaKaoToken;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
public class KaKaoUserService {

    @Value("${kakao.client-id}")
    private String clientId; // 카카오 개발자 콘솔에서 발급받은 클라이언트 ID
    @Value("${kakao.redirect-url}")
    private String redirectUri; // 카카오 로그인 후 리다이렉트될 URI
    private final JpaUserRepository userRepository;
    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;

    @Autowired
    public KaKaoUserService(
        JpaUserRepository userRepository,
        RestTemplateBuilder restTemplateBuilder,
        ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.restTemplateBuilder = restTemplateBuilder;
        this.objectMapper = objectMapper;
    }

    public KaKaoToken getKaKaoToken(String authorizedCode) {
        var url = "https://kauth.kakao.com/oauth/token";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizedCode);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
            String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                String accessToken = rootNode.path("access_token").asText();
                String refreshToken = rootNode.path("refresh_token").asText();

                return new KaKaoToken(accessToken, refreshToken);

            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "토큰을 처리하는 중 오류가 발생했습니다.");
            }
        } else {
            System.out.println("응답 실패: " + response.getStatusCode());
            throw new BusinessException(ErrorCode.FORBIDDEN, "사용자 권한이 없습니다.");
        }
    }

    public User findUserByKaKaoAccessToken(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request,
            String.class);

        User findUser = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // 사용자 정보 추출
                String id = rootNode.path("id").asText(); // unique
                String nickname = rootNode.path("properties").path("nickname").asText();
                String email = rootNode.path("kakao_account").path("email").asText();

                System.out.println("id = " + id);
                System.out.println("nickname = " + nickname);
                System.out.println("email = " + email);

                findUser = userRepository.findByEmail(email);

                // 유저 정보 없으면 자동 회원 가입
                if (findUser == null) {
                    findUser = createUser(email);
                }

            } catch (JsonProcessingException e) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "json 데이터 형식이 올바르지 않습니다.");
            }
        }
        return findUser;
    }

    private User createUser(String email) {
        String salt = UUID.randomUUID().toString();
        String ori_password = salt + UUID.randomUUID().toString();
        String hashed_password = ori_password; // hash 과정 생략

        User user = new User(email, hashed_password);
        userRepository.save(user);
        return userRepository.findByEmail(email);
    }

}
