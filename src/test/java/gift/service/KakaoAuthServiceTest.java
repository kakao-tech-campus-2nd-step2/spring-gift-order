package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.users.kakao.KakaoAuthService;
import gift.users.kakao.KakaoProperties;
import gift.users.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoAuthServiceTest {

    private MockRestServiceServer server;
    private KakaoAuthService kakaoAuthService;
    private UserService userService = mock(UserService.class);
    private ObjectMapper objectMapper;
    private RestClient.Builder restClientBuilder;
    @Autowired
    private KakaoProperties kakaoProperties;

    @BeforeEach
    void beforeEach() {
        objectMapper = new ObjectMapper();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        restClientBuilder = RestClient.builder()
            .requestFactory(factory)
            .defaultHeaders(headers -> {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            });
        server = MockRestServiceServer.bindTo(restClientBuilder).build();
        kakaoAuthService = new KakaoAuthService(kakaoProperties, restClientBuilder, objectMapper,
            userService);
    }

    @Test
    @DisplayName("카카오 로그인")
    void kakaoCallBack() throws JsonProcessingException {
        //given
        String code = "test_code";
        String tokenResponse = "{\"access_token\":\"test_access_token\"}";
        String userResponse = "{\"id\":1}";

        server.expect(requestTo(kakaoProperties.tokenUrl()))
            .andExpect(method(POST))
            .andRespond(withSuccess(tokenResponse, MediaType.APPLICATION_JSON));

        server.expect(requestTo(kakaoProperties.userUrl()))
            .andExpect(method(POST))
            .andRespond(withSuccess(userResponse, MediaType.APPLICATION_JSON));

        when(userService.findByKakaoIdAndRegisterIfNotExists("1")).thenReturn(1L);
        when(userService.loginGiveToken("1")).thenReturn("user_token");

        //when
        String result = kakaoAuthService.kakaoCallBack(code);

        //then
        assertThat(result).isEqualTo("user_token");
    }
}
