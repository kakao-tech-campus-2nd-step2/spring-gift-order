package gift;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Test
    void test1(){
        var url = "https://kauth/kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "c19991678d82e3768f54412c03c96c96");
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", "GxO2mzicTHnpXItBJA3UOs5EAdDjnWOb3JwbeUlXUGVSD652mo6ItgAAAAQKKiUOAAABkNg2NAEhI_W2iNNaeg");
        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
