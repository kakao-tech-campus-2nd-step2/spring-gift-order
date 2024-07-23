package gift.study;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix="kakao")
record KakaoProperties(String clientId, String redirectUrl){}

@SpringBootTest
public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Autowired
    private KakaoProperties properties;

    @Test
    void test2(){
        assertThat(properties.clientId()).isNotEmpty();
        assertThat(properties.redirectUrl()).isNotEmpty();
        System.out.println(properties);
    }

    @Test
    void test1(){
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static @NotNull LinkedMultiValueMap<String, String> createBody(){
        var body = new LinkedMultiValueMap<String, String>();
        var properties = new KakaoProperties("8447677252f9163f32c6238ca9ecb54b", "http://localhost:8080");
        var code = "GxSPbJCDfTiMa79zYI_HmT9oKmg97PkHHelfiupShRHG9VSC-weYvAAAAAQKPCKcAAABkNg9tFjSDh85zpcCzQ";
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
