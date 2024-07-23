package gift.service;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gift.exception.KakaoNotEnabledException;

@Service
public class KakaoAuthService {
	
	@Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    
    private final RestTemplate restTemplate;
    
    public KakaoAuthService(RestTemplate restTemplate) {
    	this.restTemplate = restTemplate;
    }
	
    public Map<String, String> getAccessToken(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
            request,
            new ParameterizedTypeReference<Map<String, String>>() {}
        );

        validateResponse(response);

        return response.getBody();
    }
    
    private void validateResponse(ResponseEntity<Map<String, String>> response) {
        if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new KakaoNotEnabledException("Forbidden: [spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.");
        }
    }
}
