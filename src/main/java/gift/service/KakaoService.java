package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.model.User;
import gift.repository.UserRepository;
import gift.security.JwtTokenProvider;
import gift.security.KakaoTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class KakaoService {

    private static final Logger LOGGER = Logger.getLogger(KakaoService.class.getName());

    private final RestClient client = RestClient.builder().build();
    private final UserRepository userRepository;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public KakaoService(
            KakaoTokenProvider kakaoTokenProvider,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            UserService userService
    ) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public String login(String code) throws Exception {
        LOGGER.info("Logging in with code: " + code);
        String token = kakaoTokenProvider.getToken(code);
        LOGGER.info("Obtained token: " + token);
        User kakaoUser = getKakaoUserInfo(token);
        LOGGER.info("Kakao user info: " + kakaoUser.getEmail());

        User user = userService.loginOrRegisterUser(kakaoUser.getEmail(), token);
        LOGGER.info("Saved token for user: " + user.getEmail());

        return jwtTokenProvider.createToken(user.getEmail());
    }

    public User getKakaoUserInfo(String accessToken) throws Exception {
        var url = "https://kapi.kakao.com/v2/user/me";

        try {
            ResponseEntity<String> entity = client.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(String.class);

            String resBody = entity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resBody);
            String email = jsonNode.path("kakao_account").path("email").asText();
            String password = UUID.randomUUID().toString();  // 랜덤 패스워드 생성

            return new User(email, password);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("카카오 사용자 정보 가져오기 실패: " + e.getResponseBodyAsString(), e);
        }
    }

    @Transactional
    public void sendMessageToMe(String email, String message) throws Exception {
        LOGGER.info("Sending message to user: " + email);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || user.getKakaoAccessToken() == null) {
            throw new RuntimeException("사용자에 대한 액세스 토큰이 없습니다.");
        }

        String accessToken = user.getKakaoAccessToken();

        String templateObject = "{ \"object_type\": \"text\", \"text\": \"" + message + "\", \"link\": { \"web_url\": \"http://localhost:8080\", \"mobile_web_url\": \"http://localhost:8080\" }}";

        try {
            kakaoTokenProvider.sendMessage(accessToken, templateObject);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("this access token does not exist")) {
                throw new RuntimeException("액세스 토큰이 유효하지 않습니다. 다시 로그인해 주세요.");
            } else {
                throw e;
            }
        }
    }
}