package gift.service;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gift.entity.User;
import gift.exception.UnauthorizedException;
import gift.repository.UserRepository;

@Service
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.auth-url}")
    private String authUrl;
    
    @Value("${kakao.token-info-url}")
    private String tokenInfoUrl;
    
    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public KakaoAuthService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public Map<String, String> getAccessToken(String authorizationCode) {
        RequestEntity<MultiValueMap<String, String>> request = authRequest(authorizationCode);
        ResponseEntity<Map<String, String>> response = ErrorHandling(request);
        Map<String, String> token = response.getBody();
        
        String accessToken = token.get("access_token");
        processUserLogin(accessToken);
        
        return token;
    }

    private RequestEntity<MultiValueMap<String, String>> authRequest(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = createBody(authorizationCode);
        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(authUrl));
    }

    private MultiValueMap<String, String> createBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);
        body.add("scope", "account_email");
        return body;
    }

    private ResponseEntity<Map<String, String>> ErrorHandling(RequestEntity<MultiValueMap<String, String>> request) {
        return restTemplate.exchange(request, new ParameterizedTypeReference<Map<String, String>>() {});
    }
    
    public String parseKakaoToken(String token) {
        RequestEntity<Void> request = tokenInfoRequest(token);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(request, new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("kakao_account")) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        if (kakaoAccount == null || !kakaoAccount.containsKey("email")) {
            throw new UnauthorizedException("Email not found in token");
        }
        return (String) kakaoAccount.get("email");
    }
    
    private void processUserLogin(String accessToken) {
        String email = parseKakaoToken("Bearer " + accessToken);
        findOrRegisterUser(email);
    }

    private User findOrRegisterUser(String email) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(email, "default_password123");
            userRepository.save(newUser);
            return newUser;
        });
    }
    
    private RequestEntity<Void> tokenInfoRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return new RequestEntity<>(headers, HttpMethod.GET, URI.create(tokenInfoUrl));
    }
}
