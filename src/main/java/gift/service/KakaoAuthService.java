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

@Service
public class KakaoAuthService {
	
	@Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    
    @Value("${kakao.auth-url}")
    private String authUrl;
    
    private final RestTemplate restTemplate;
    
    public KakaoAuthService(RestTemplate restTemplate) {
    	this.restTemplate = restTemplate;
    }
	
    public Map<String, String> getAccessToken(String authorizationCode) {
    	RequestEntity<MultiValueMap<String, String>> request = buildAuthRequest(authorizationCode);
    	ResponseEntity<Map<String, String>> response = ErrorHandling(request);
        return response.getBody();
    }
    
    private RequestEntity<MultiValueMap<String, String>> buildAuthRequest(String authorizationCode) {
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
        return body;
    }
    
    private ResponseEntity<Map<String, String>> ErrorHandling(RequestEntity<MultiValueMap<String, String>> request) {
    	return restTemplate.exchange(request, new ParameterizedTypeReference<Map<String, String>>() {});
    }
}
