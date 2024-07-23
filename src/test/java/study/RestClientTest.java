package study;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.service.dto.KakaoTokenDto;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "kakao")
record KakaoProperties(
        String clientId,
        String redirectUrl,
        String tokenUrl,
        String memberInfoUrl
) {}

@ActiveProfiles("test")
@SpringBootTest
public class RestClientTest {
    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KakaoProperties properties;

    @MockBean
    private RedissonClient redissonClient;


    @Test
    void test0() {
        System.out.println(properties);
    }

    @Test
    void getKakaoAccessToken() {
        var url = properties.tokenUrl();
        var code = "78RmEworRfRtyhb6EanyFU_9RWox5DRxHVObH3128eHaCTIV-FEinAAAAAQKKiWQAAABkN8NH4oh5oEAb4_jFQ";
        final var body = createBody(code);
        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)     // request body
                .exchange((req, res) -> {
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            if (res.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                return objectMapper.readValue(res.getBody(), KakaoTokenDto.class);
            }
            return "";
        });
        System.out.println(response);
    }

    @Test
    void getKakaoMemberInfo() {
        var access_token = "EXzy9fHlc7wjc6GlfR1uRVo9yKYPTqJHAAAAAQorDR8AAAGQ3w6vZyn2EFsnJsRZ?";
        var response = client.post()
                .uri(URI.create(properties.memberInfoUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + access_token)
                .retrieve()
                .toEntity(String.class);

        System.out.println(response);
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
