package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.oauth.properties.KakaoProperties;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
public class OAuthControllerTest {

    @Autowired
    private  KakaoProperties properties;

    private final RestClient client = RestClient.builder().build();

    @Test
    void getUrlTest(){
        UriComponentsBuilder UriBuilder = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("kauth.kakao.com")
            .path("/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId()+"1")
            .queryParam("redirect_uri", properties.redirectUrl());

        var response = client.get()
            .uri(UriBuilder.toUriString())
            .retrieve()
            .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    @DisplayName("토큰 발급 테스트")
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
        String code = "";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
