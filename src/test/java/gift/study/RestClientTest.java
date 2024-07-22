package gift.study;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> body = createBody();

        ResponseEntity<String> response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static LinkedMultiValueMap<String, String> createBody() {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "client_id");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "인가코드");
        return body;
    }

}
