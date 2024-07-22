package gift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1(){

        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "c19991678d82e3768f54412c03c96c96");
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", "n_itDgc5mWkI7EMxqpUVOIqHOmET7bXJnj4KZpQvrheA0RHntex7gwAAAAQKKcleAAABkNgo5L0SmUam6ZdnFg");

        var request = new RequestEntity<>(body,headers, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request,String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);

    }
}
