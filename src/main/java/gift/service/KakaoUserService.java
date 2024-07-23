package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import gift.domain.User;
import gift.repository.UserRepository;
import gift.security.JwtTokenProvider;
import gift.security.KakaoTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.UUID;

@Service
public class KakaoUserService {
    private final RestClient client = RestClient.builder().build();
    private final UserRepository userRepository;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoUserService(
            KakaoTokenProvider kakaoTokenProvider,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository
    ) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Transactional
    public Token login(String code) throws JsonProcessingException {
        String token = kakaoTokenProvider.getToken(code);
        User kakaoUser = getKakaoUserInfo(token);

        return jwtTokenProvider.makeToken(new UserRequest(
                kakaoUser.getUserId(), kakaoUser.getEmail(), kakaoUser.getPassword()
        ));
    }

    public User getKakaoUserInfo(String access_token) throws JsonProcessingException {
        var url = "https://kapi.kakao.com/v2/user/me";

        ResponseEntity<String> entity = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + access_token)
                .retrieve()
                .toEntity(String.class);

        String resBody = entity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);
        String id = jsonNode.get("id").asText();

        if(userRepository.existsByUserId(id)){
            return userRepository.findByUserId(id);
        }
        User user = new User(id,id+"@email.com", UUID.randomUUID().toString());
        user.insertToken(access_token);
        userRepository.save(user);

        return user;
    }
}
