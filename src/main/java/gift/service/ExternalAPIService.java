package gift.service;

import gift.repository.Properties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;

@Service
public class ExternalAPIService {

    String kakaoOauthAuthorizeUrl= "https://kauth.kakao.com/oauth/authorize";
    String kakaoOauthTokenUrl = "https://kauth.kakao.com/oauth/token";
    Properties properties;

    public void getKakaoToken() {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String,String>();
        body.add("grant_type","authorization_code");
        body.add("client_id",properties.getClientId());
        body.add("redirect_uri",properties.getRedirectUri());
        body.add("code",properties.getAuthorizationCode());
        var request =new RequestEntity<>(body,headers, HttpMethod.POST, URI.create(kakaoOauthTokenUrl));
    }


}

