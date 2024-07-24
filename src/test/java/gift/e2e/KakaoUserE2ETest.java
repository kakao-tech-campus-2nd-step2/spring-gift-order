package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import gift.service.KakaoUserService;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class KakaoUserE2ETest {

    private final String testCode = "testCode";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KakaoProperties kakaoProperties;
    @Autowired
    private KakaoUserService kakaoUserService;
    private MockWebServer mockServer;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        restTemplate = new RestTemplate();
    }

    @AfterEach
    void shutDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    @DisplayName("인가 코드 테스트")
    void authizationTest() {
        String baseUri = "/oauth/authorize";
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUri);
        URI authCodeRequestUri = uriBuilderFactory.builder()
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
            .queryParam("client_id", kakaoProperties.getClientId())
            .build();
        String mockWebServerUri = mockServer.url(authCodeRequestUri.toString()).toString();
        mockServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.FOUND.value())
            .addHeader("Location", kakaoProperties.getRedirectUri() + "?code=" + testCode)
        );

        ResponseEntity<String> response = getResponse(mockWebServerUri);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Map<String, String> queryParams = UriComponentsBuilder.fromUri(
                Objects.requireNonNull(response.getHeaders().getLocation()))
            .build()
            .getQueryParams()
            .toSingleValueMap();
        assertThat(queryParams.get("code")).isEqualTo(testCode);
    }

    @Test
    @DisplayName("accessToken 테스트")
    void getAccessTokenTest() throws Exception {
        KakaoTokenResponse testResponse = new KakaoTokenResponse("testAccessToken", "bearer",
            "test", 21599, "test");
        String tokenRequestUri = "/oauth/token";
        String mockWebServerUri = mockServer.url(tokenRequestUri).toString();
        mockServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setBody(objectMapper.writeValueAsString(testResponse))
        );

        ResponseEntity<String> response = getResponse(mockWebServerUri);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        KakaoTokenResponse tokenResponse = objectMapper.readValue(response.getBody(),
            KakaoTokenResponse.class);
        assertThat(tokenResponse.accessToken()).isEqualTo(testResponse.accessToken());
        assertThat(tokenResponse.accessToken()).isEqualTo("testAccessToken");
    }

    @Test
    @DisplayName("accessToken 오류 테스트")
    void accessTokenErrorTest() {

        assertThatThrownBy(() -> kakaoUserService.getAccessToken(testCode))
            .isInstanceOf(HttpClientErrorException.class)
            .hasMessage(
                "400 Bad Request: \"{\"error\":\"invalid_grant\",\"error_description\":\"authorization code not found for code="
                    + testCode + "\",\"error_code\":\"KOE320\"}\"");

    }

    private ResponseEntity<String> getResponse(String mockWebServerUri) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", testCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(body,
            headers);

        return restTemplate.exchange(mockWebServerUri, HttpMethod.POST,
            requestEntity, String.class);
    }
}
