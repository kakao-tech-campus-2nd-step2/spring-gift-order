package gift.service;


import gift.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectUri;

    @Autowired
    private RestTemplate restTemplate;

    public String getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .toUriString();

        Map<String, String> response = restTemplate.postForObject(url, null, HashMap.class);

        return response.get("access_token");
    }

    public JSONObject getUserInfo(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(reqURL, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new JSONObject(response.getBody());
        }

        throw new RuntimeException("Failed to get user info from Kakao");
    }

    public String login(String code) throws MemberNotFoundException {
        String accessToken = getKakaoAccessToken(code);
        JSONObject userInfo = getUserInfo(accessToken);

        String email= null;
        if (userInfo.has("kakao_account")) {
            JSONObject kakaoAccount = userInfo.getJSONObject("kakao_account");
            if (kakaoAccount.has("email")) {
                email = kakaoAccount.getString("email");
            }
        }

        if (email == null) {
            throw new MemberNotFoundException("Email not found");
        }
        // 로그인 성공 처리
        return accessToken;
    }

}
