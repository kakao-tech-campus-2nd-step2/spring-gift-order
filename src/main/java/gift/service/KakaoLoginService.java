package gift.service;

import gift.DTO.KakaoToken;
import org.springframework.beans.factory.annotation.Autowired;
import gift.DTO.KakaoProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoLoginService {

    private static String KAKAO_API_URL = "https://kapi.kakao.com";

    @Autowired
    private KakaoProperties kakaoProperties;

    private RestClient restClient = RestClient.builder().build();

    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri="
                + kakaoProperties.redirectUrl() + "&client_id=" + kakaoProperties.restApiKey();
    }

    public KakaoToken getToken(String code){
        System.out.println("code = " + code);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoToken.class)
                .getBody();
    }

    public String getKakaoUserInfo(String accessToken) {
        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("kapi.kakao.com")
                .path("/v2/user/me")
//                .queryParam("property_keys", "[\"kakao_account.name\"]")
                .build()
                .toUriString();

        System.out.println("uri = " + uri);

        return restClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
