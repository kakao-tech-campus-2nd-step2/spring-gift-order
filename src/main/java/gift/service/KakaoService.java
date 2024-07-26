package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.model.User;
import gift.repository.UserRepository;
import gift.security.JwtTokenProvider;
import gift.security.KakaoTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.UUID;

@Service
public class KakaoService {

    private final RestClient client = RestClient.builder().build();
    private final UserRepository userRepository;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoService(
            KakaoTokenProvider kakaoTokenProvider,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository
    ) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Transactional
    public String login(String code) throws Exception {
        String token = kakaoTokenProvider.getToken(code);
        User kakaoUser = getKakaoUserInfo(token);

        return jwtTokenProvider.createToken(kakaoUser.getEmail());
    }

    public User getKakaoUserInfo(String accessToken) throws Exception {
        var url = "https://kapi.kakao.com/v2/user/me";

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

        if (userRepository.existsByEmail(email)) {
            return userRepository.findByEmail(email);
        }

        User user = new User(email, UUID.randomUUID().toString());
        userRepository.save(user);

        return user;
    }
}