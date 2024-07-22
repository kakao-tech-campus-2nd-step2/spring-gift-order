package gift;

import gift.config.RestTemplateConfig;
import gift.service.kakao.KakaoLoginService;
import gift.service.kakao.KakaoProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = KakaoLoginService.class)
public class KakaoLoginServiceApiRequestTest {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private KakaoProperties kakaoProperties;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("Access Token 을 가져온다.")
    @Test
    void getAccessToken() throws Exception {
        //given
        String code = "test-code";
        String expectedResponseBody = "{ \"access_token\": \"test-access-token\"}";

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(content().string("grant_type=authorization_code&client_id&redirect_uri&code=test-code"))
                .andRespond(withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        //when
        String accessToken = kakaoLoginService.getAccessToken(code);

        //then
        assertThat(accessToken).isEqualTo("test-access-token");
        mockServer.verify();
    }

}
