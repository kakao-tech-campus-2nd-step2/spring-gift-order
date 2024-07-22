package gift.study;

import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1(){
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "8447677252f9163f32c6238ca9ecb54b");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "IU9oJDtYt1RX9Ow9BpJuxkP2DEKz8ajTZ1ERslROIehWdhTSoplYOgAAAAQKKiWOAAABkNgma6DGDcCf5rkkeA");
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

}
