package gift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;

@Service
public class KakaoLoginService {

    @Value("${kakao.login.REST-API-KEY}")
    private String loginRestApiKey;

    @Value("${kakao.login.REDIRECT-URI}")
    private String loginRedirectUri;

    public String makeKakaoAuthorizationURI() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=")
                .append(loginRestApiKey)
                .append("&redirect_uri=")
                .append(loginRedirectUri);

        return stringBuilder.toString(); // 여기로 get 하면 query parameter로 인가 코드가 날아옴.
    }

    public RequestEntity<LinkedMultiValueMap<String, String>> getKakaoAuthorizationToken(String code){
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", loginRestApiKey);
        body.add("redirect_uri", loginRedirectUri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        return request;
    }



}
