package gift;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

//    @Test
//    void test1() {
//        String url = "https://kauth.kakao.com/oauth/token";
//        var headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//        var body = new LinkedMultiValueMap<String, String>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", "1fdc4eeb45ccb5f6a2f6e005c7810546");
//        body.add("redirect_uri", "http://localhost:8080/redirect/kakao");
//        body.add("code", "ndBaC3Nu2YHR-KBm2ZuhREXoJaD781q42ERJ9OGOdtenslKuF9GUxgAAAAQKKiVSAAABkOpPrx-IenTzhLqDRQ");
//        RequestEntity request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
//        var response = client.exchange(request, String.class);
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
//    }
}
