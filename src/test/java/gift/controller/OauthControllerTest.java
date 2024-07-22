package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.oauth.properties.KakaoProperties;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class OauthControllerTest {

    @Autowired
    private  KakaoProperties properties;

    private final RestClient client = RestClient.builder().build();
    @Test
    void test(){
        var url = "https://kauth.kakao.com/oauth/token";
        var body = creatBody();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private LinkedMultiValueMap<String, String> creatBody() {
        String code = "eEr4sZhJa9_NjMA_gB239L0F5o9ejaUmF18ZPNP-EZKrFtQB2QiFLwAAAAQKKw0fAAABkNm3M7Ae0jm_MNo9Pw";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
