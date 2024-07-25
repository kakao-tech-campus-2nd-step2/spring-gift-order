package gift.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gift.entity.KakaoProperties;
import gift.entity.User;
import gift.entity.UserDTO;
import gift.exception.ResourceNotFoundException;
import gift.repository.UserRepository;
import gift.util.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private KakaoProperties kakaoProperties;

    private final RestClient client = RestClient.builder().build();

    private final UserRepository userRepository;
    private final UserUtility userUtility;

    @Autowired
    public UserService(UserRepository userRepository, UserUtility userUtility) {
        this.userRepository = userRepository;
        this.userUtility = userUtility;
    }

    public User findOne(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public String signup(UserDTO userDTO) {
        Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        User savedUser = userRepository.save(new User(userDTO));
        return userUtility.makeAccessToken(savedUser);
    }

    public String login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email does not exist"));
        if (!userDTO.getPassword().equals(user.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match");
        return userUtility.makeAccessToken(user);
    }

    public Map<String, String> kakaoLogin(String code) {
        // access token 받아오기
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", code);

        String accessTokenResponse = client.post()
                .uri(URI.create("https://kauth.kakao.com/oauth/token"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(String.class);

        JsonObject jsonObject = JsonParser.parseString(accessTokenResponse).getAsJsonObject();
        String kakaoAccessToken = jsonObject.get("access_token").getAsString();

        // 유저 정보 받아오기
        String userDataResponse = client.post()
                .uri(URI.create("https://kapi.kakao.com/v2/user/me"))
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .body(String.class);

        jsonObject = JsonParser.parseString(userDataResponse).getAsJsonObject();
        JsonObject kakaoObject = jsonObject.get("kakao_account").getAsJsonObject();
        String email = kakaoObject.get("email").getAsString();

        if (!userRepository.existsByEmail(email)) {
            userRepository.save(new User(email, ""));
        }

        User user = userRepository.findByEmail(email).get();
        String accessToken = userUtility.makeAccessToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        response.put("accessToken", accessToken);
        response.put("kakaoAccessToken", kakaoAccessToken);

        return response;
    }
}
