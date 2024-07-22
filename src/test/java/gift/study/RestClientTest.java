package gift.study;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@ConfigurationPropertiesScan
@ConfigurationProperties("kakao")
record KakaoProperties(
    String clientId,
    String redirectUrl
) {
}

@ActiveProfiles("test")
@SpringBootTest
public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Autowired
    private KakaoProperties properties;

    @Test
    void test2() {
        assertThat(properties.clientId()).isNotEmpty();
        assertThat(properties.redirectUrl()).isNotEmpty();
        assertThat(properties.redirectUrl()).isEqualTo("http://localhost:8080");
    }

    @Test
    void test1() {
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

    private static @NotNull LinkedMultiValueMap<String, String> createBody() {
        var code = "v_UwXkjR5JJLTf8P82GBKlKr0dnHFT6st5jsEEIQlvEMq2Z0gKuCAwAAAAQKPCSbAAABkNjYA4FAPV-WDrAHcw";
        var properties = new KakaoProperties("d8e855663ae6bf0fba3c1493efa9086e", "http://localhost:8080");
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
