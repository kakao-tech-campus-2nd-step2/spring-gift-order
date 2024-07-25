package gift.service;

import gift.exception.MissingAuthorizationCodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static gift.constants.Messages.MISSING_AUTHORIZATION_CODE;

@Service
public class KakaoMemberService {

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    private final RestTemplate restTemplate;

    public KakaoMemberService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getKakaoCodeUrl() {
        String baseUrl = "https://kauth.kakao.com/oauth/authorize";
        return baseUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
    }

    public String getKakaoToken(String code) {
        if (code == null || code.isEmpty()) {
            throw new MissingAuthorizationCodeException(MISSING_AUTHORIZATION_CODE);
        }

        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = restTemplate.exchange(request, Map.class);

        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");
        System.out.println("Access Token: " + accessToken);
        return accessToken;
    }

    public String getKakaoUserEmail(String token) {
        var url = "https://kapi.kakao.com/v2/user/me";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        var response = restTemplate.exchange(request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        String id = responseBody.get("id").toString();
        String userEmail = id+"@kakao.com";
        return userEmail;
    }

    public boolean isKakaoTokenValid(String token){
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        try {
            var response = restTemplate.exchange(request, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sendKakaoMessage(String accessToken, String message) {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        var body = new LinkedMultiValueMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"object_type\":\"text\",\"text\":\"")
                .append(message)
                .append("\",\"link\":{\"web_url\":\"\"}}");
        body.add("template_object", sb.toString());

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = restTemplate.exchange(request, Map.class);
    }
}