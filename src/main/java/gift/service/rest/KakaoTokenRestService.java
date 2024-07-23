package gift.service.rest;

import gift.config.properties.KakaoProperties;
import gift.dto.KakaoAuthInformation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoTokenRestService {

    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;

    public KakaoTokenRestService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getTokenWithCode(String code) {
        var response = getTokenResponse(code);
        return (String) response.get("access_token");
    }

    public KakaoAuthInformation getAuthInformationWithToken(String accessToken) {
        var response = getKakaoAuthResponse(accessToken);
        var kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        var profile = (Map<String, Object>) kakaoAccount.get("profile");
        var name = (String) profile.get("nickname");
        var email = (String) kakaoAccount.get("email");
        return KakaoAuthInformation.of(name, email);
    }

    private Map getTokenResponse(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", kakaoProperties.grantType());
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(Map.class)
                .getBody();

        return response;
    }

    private Map getKakaoAuthResponse(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var header = "Bearer " + accessToken;

        var response = client.get()
                .uri(URI.create(url))
                .header("Authorization", header)
                .retrieve()
                .toEntity(Map.class)
                .getBody();
        System.out.println(response);
        return response;
    }
}
