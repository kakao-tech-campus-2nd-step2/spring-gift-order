package gift.study;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ConfigurationProperties(prefix = "kakao")
record KakaoProperties(
        String clientId,
        String redirectUrl
){}

public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        LinkedMultiValueMap<String, String> body = createBody();
        var request = new RequestEntity<>(body, header, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static LinkedMultiValueMap<String, String> createBody() {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        KakaoProperties properties = new KakaoProperties("clientID", "http://localhost:8080");
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        
        return body;
    }
}
