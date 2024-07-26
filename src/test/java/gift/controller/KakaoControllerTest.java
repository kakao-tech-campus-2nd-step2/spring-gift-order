//package gift.controller;
//
//import gift.service.KakaoProperties;
//import gift.service.KakaoService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.test.web.client.match.MockRestRequestMatchers;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.web.client.RestTemplate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = KakaoController.class)
//@ActiveProfiles("test")
//@Import(KakaoControllerTest.TestConfig.class)
//public class KakaoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private KakaoProperties kakaoProperties;
//
//    @Autowired
//    private KakaoService kakaoService;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private MockRestServiceServer mockServer;
//
//    @BeforeEach
//    void setUp() {
//        // MockRestServiceServer를 사용하여 RestTemplate 모킹
//        mockServer = MockRestServiceServer.createServer(restTemplate);
//    }
//
//    @Test
//    void testKakaoLogin() throws Exception {
//        // Given
//        String clientId = kakaoProperties.getClientId();
//        String redirectUri = kakaoProperties.getRedirectUri();
//
//        // When
//        MvcResult result = mockMvc.perform(get("/login"))
//                .andExpect(status().is3xxRedirection())
//                .andReturn();
//
//        // Then
//        String redirectedUrl = result.getResponse().getRedirectedUrl();
//        String expectedUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
//                + clientId + "&redirect_uri=" + redirectUri;
//
//        assertThat(redirectedUrl).isEqualTo(expectedUrl);
//    }
//
//    @Test
//    void testCallback() throws Exception {
//        // Given
//        String code = "test-code";
//        String accessToken = "test-access-token";
//
//        // Mocking Kakao API response
//        mockServer.expect(MockRestRequestMatchers.requestTo("https://kauth.kakao.com/oauth/token"))
//                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
//                .andExpect(MockRestRequestMatchers.content().contentType("application/x-www-form-urlencoded;charset=UTF-8"))
//                .andRespond(withSuccess("{\"access_token\":\"" + accessToken + "\"}", MediaType.APPLICATION_JSON));
//
//        // When
//        MvcResult result = mockMvc.perform(get("/callback").param("code", code))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/home"))
//                .andReturn();
//
//        // Then
//        assertThat(result.getResponse().getStatus()).isEqualTo(302);
//        assertThat(result.getResponse().getRedirectedUrl()).isEqualTo("/home");
//
//        // Verify mock server
//        mockServer.verify();
//    }
//
//    @TestConfiguration
//    static class TestConfig {
//
//        @Bean
//        public KakaoProperties kakaoProperties() {
//            return new KakaoProperties();
//        }
//
//        @Bean
//        public KakaoService kakaoService(RestTemplate restTemplate) {
//            return new KakaoService(restTemplate);
//        }
//
//        @Bean
//        public RestTemplate restTemplate() {
//            return new RestTemplate();
//        }
//    }
//}
