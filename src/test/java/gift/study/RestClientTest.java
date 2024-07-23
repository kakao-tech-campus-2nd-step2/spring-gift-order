package gift.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Test
    void test1(){
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "104672445eed390cc023c9380c48d746");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "w-ICT4eCX517Hk1NgUFh2WCOv2gVqRvJMi5t_CaNpSM_HXrPBQ70oAAAAAQKKiWQAAABkNg8pdoBl6J2VXah6g");

        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        
        System.out.println(response);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
