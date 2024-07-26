package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.KakaoProperties;
import gift.service.KakaoService;
import gift.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = KakaoController.class)
@ActiveProfiles("test")
public class KakaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoProperties kakaoProperties;

    @MockBean
    private KakaoService kakaoService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testKakaoLogin() throws Exception {
        // Given
        String clientId = "test-client-id";
        String redirectUri = "http://localhost:8080/callback";
        String authUrl = "https://kauth.kakao.com/oauth/authorize";
        Mockito.when(kakaoProperties.getClientId()).thenReturn(clientId);
        Mockito.when(kakaoProperties.getRedirectUri()).thenReturn(redirectUri);
        Mockito.when(kakaoProperties.getAuthUrl()).thenReturn(authUrl);

        // When
        MvcResult result = mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        // Then
        String redirectedUrl = result.getResponse().getRedirectedUrl();
        String expectedUrl = authUrl + "?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri;

        assertThat(redirectedUrl).isEqualTo(expectedUrl);
    }

    @Test
    void testCallback() throws Exception {
        // Given
        String code = "test-code";
        String accessToken = "test-access-token";
        Mockito.when(kakaoService.getAccessToken(code)).thenReturn(accessToken);

        // When
        MvcResult result = mockMvc.perform(get("/callback").param("code", code))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(302);
        assertThat(result.getResponse().getRedirectedUrl()).isEqualTo("/home");
    }

    // 사용자 정보 받기, 메시지 보내기 테스트는 추가하겠습니다.
}