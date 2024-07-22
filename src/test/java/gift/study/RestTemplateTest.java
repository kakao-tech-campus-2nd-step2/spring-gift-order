package gift.study;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {

    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "5bc075cfab5ae4b53f2d3eb6e44ed01f");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "18UXgWckmwJTAXEG7P19uj449fJiSIan0S89rhhjz619en0YkygXnwAAAAQKKiVOAAABkNgpamltZc76WqiBKA");
        var request = new RequestEntity<>(body,header, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request,String.class);
        System.out.println(response);

    }

}
