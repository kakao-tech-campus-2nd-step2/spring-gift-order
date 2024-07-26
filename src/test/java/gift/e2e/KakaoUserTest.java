package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gift.config.ClientConfig;
import gift.user.client.KakaoLoginClient;
import gift.user.config.KakaoProperties;
import gift.user.dto.response.KakaoTokenResponse;
import gift.user.dto.response.KakaoUserInfoResponse;
import java.net.URI;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Import({ClientConfig.class, KakaoProperties.class, KakaoLoginClient.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/initialize.sql"})
public class KakaoUserTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int port;
    @Autowired
    private KakaoProperties kakaoProperties;
    @Autowired
    private KakaoLoginClient kakaoLoginClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(RestClient.builder()).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void Port() {
        assertThat(port).isNotZero();
    }

    @Test
    @DisplayName("kakao login")
    void kakaoLogin() throws JsonProcessingException {
        // given
        var tokenResponse = new KakaoTokenResponse(
            "bearer",
            "token",
            null,
            null,
            null,
            null,
            null
        );
        var userInfoResponse = new KakaoUserInfoResponse(
            1234L,
            LocalDateTime.now()
        );

        var bodyForCode = new LinkedMultiValueMap<String, String>();
        bodyForCode.add("code", "test_code");
        System.out.println(kakaoProperties.clientId() + "\n" + kakaoProperties.redirectUri());
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/authorize"
                + "?scope=talk_message"
                + "&response_type=code"
                + "&redirect_uri=" + kakaoProperties.redirectUri()
                + "&client_id=" + kakaoProperties.clientId()))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(objectMapper.writeValueAsString(bodyForCode))
            );

        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", "test_code");

        String requestBody = objectMapper.writeValueAsString(body);

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(content().json(requestBody))
            .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(tokenResponse))
            );

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me?property_keys=[]"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
            .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(userInfoResponse))
            );
        var url = "http://localhost:" + port + "/api/users/auth/kakao/code?code=test_code";
        var request = new RequestEntity<>(HttpMethod.GET, URI.create(url));

        // when
        var actualResponse = kakaoLoginClient.getKakaoTokenResponse("test_code");

        // then
    }
}
