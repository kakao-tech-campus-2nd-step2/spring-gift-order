package gift.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "d8e855663ae6bf0fba3c1493efa9086e");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "v_UwXkjR5JJLTf8P82GBKlKr0dnHFT6st5jsEEIQlvEMq2Z0gKuCAwAAAAQKPCSbAAABkNjYA4FAPV-WDrAHcw");
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(request);
    }

}
