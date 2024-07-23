package gift.service;

import gift.domain.KakaoProperties;
import gift.domain.KakaoTokenResponsed;
import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final RestClient client;
    private final KakaoProperties kakaoProperties;

    public KakaoService(KakaoProperties kakaoProperties){
        this.kakaoProperties = kakaoProperties;
        this.client = RestClient.builder().build();
    }

    public KakaoTokenResponsed getTokeResponse(String code){
        var url = "https://kauth.kakao.com/oauth/token";
        var body = createBody(code);
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)    //이 body는 request
            .retrieve()
            .body(KakaoTokenResponsed.class);
        return response;
    }


//    public String getAccessToken(String code){
//        var url = "https://kauth.kakao.com/oauth/token";
//        var body = createBody(code);
//        var response = client.post()
//            .uri(URI.create(url))
//            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//            .body(body)    //이 body는 request
//            .retrieve()
//            .body(Map.class);
//        return response.get("access_token").toString();
//    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());  //내애플리케이션 -> 앱키->RESTAPI키
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
