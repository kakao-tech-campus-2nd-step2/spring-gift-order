package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@ConfigurationProperties("kakao")
record KakaoProperties(
        String clientId,
        String redirectUrl
){}

@ActiveProfiles("test")
@SpringBootTest
class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Autowired
    private KakaoProperties properties;

    @ConfigurationProperties


    @Test
    void test1(){
        var url = "https://kauth.kakao.com/oauth/token";
        var code = "6mKKbQa2qRvriXvJ1E8AemtkmniawKWYS8xdEVv1th2NIqk-RUeatwAAAAQKPCQhAAABkO7cUP8e0jm_MNo9Pw";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
    }
}
