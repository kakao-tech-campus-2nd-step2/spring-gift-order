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

    public String signup(UserDTO user) {
        User savedUser = userRepository.save(new User(user));
        return userUtility.makeAccessToken(savedUser);
    }

    public String login(UserDTO user) {
        Optional<User> result = userRepository.findByEmail(user.getEmail());
        if (!result.isPresent())
            throw new ResourceNotFoundException("Email does not exist");
        User foundUser = result.get();
        if (!user.getPassword().equals(foundUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password does not match");
        return userUtility.makeAccessToken(foundUser);
    }

    public String kakaoLogin(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", code);

        String response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(String.class);

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }
}
