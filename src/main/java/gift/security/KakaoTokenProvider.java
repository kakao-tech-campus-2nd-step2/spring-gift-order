package gift.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoProperties;
import gift.domain.User;
import gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.UUID;

@Component
public class KakaoTokenProvider {
    @Value("${kakao.tokenUrl}")
    private String tokenUrl;
    @Value("${kakao.userInfoUrl}")
    private String userInfoUrl;
    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;

    public KakaoTokenProvider(KakaoProperties kakaoProperties, UserRepository userRepository) {
        this.kakaoProperties = kakaoProperties;
        this.userRepository = userRepository;
    }

    public String getToken(String code) throws JsonProcessingException {
        LinkedMultiValueMap<Object, Object> body = makeBody(code);

        ResponseEntity<String> entity = client.post()
                .uri(URI.create(tokenUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        String resBody = entity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);

        return jsonNode.get("access_token").asText();
    }

    private LinkedMultiValueMap<Object, Object> makeBody(String code){
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_url", kakaoProperties.getRedirectUrl());
        body.add("code", code);

        return body;
    }

    public User getKakaoUserInfo(String access_token) throws JsonProcessingException {
        ResponseEntity<String> entity = client.post()
                .uri(URI.create(userInfoUrl))
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
