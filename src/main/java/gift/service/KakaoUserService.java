package gift.service;

import gift.dto.KakaoProperties;
import gift.exception.KakaoOAuthException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoUserService {
    private final KakaoProperties properties;
    private final RestTemplate restTemplate;

    public KakaoUserService(KakaoProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }

    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
            + properties.redirectUrl() + "&client_id=" + properties.clientId();
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);

        try {
            return restTemplate.postForObject(url, body, String.class);
        } catch (HttpClientErrorException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }
}