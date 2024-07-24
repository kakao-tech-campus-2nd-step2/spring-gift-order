//package gift.controller;
//
//import gift.service.KakaoProperties;
//import gift.service.KakaoService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan.Filter;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.context.annotation.FilterType;
//
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = KakaoController.class,
//        excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {gift.config.SecurityConfig.class}))
//public class KakaoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private KakaoService kakaoService;
//
//    @MockBean
//    private KakaoProperties kakaoProperties;
//
//    @Test
//    @WithMockUser
//    void testKakaoLogin() throws Exception {
//        // Given
//        String clientId = removeNewlines("test-client-id");
//        String redirectUri = removeNewlines("http://localhost:8080/callback");
//        Mockito.when(kakaoProperties.getClientId()).thenReturn(clientId);
//        Mockito.when(kakaoProperties.getRedirectUri()).thenReturn(redirectUri);
//
//        // When
//        MvcResult result = mockMvc.perform(get("/login"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
//                        + URLEncoder.encode(clientId, StandardCharsets.UTF_8.toString())
//                        + "&redirect_uri="
//                        + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString())))
//                .andReturn();
//
//        // Then
//        assertThat(result.getResponse().getStatus()).isEqualTo(302);
//        assertThat(result.getResponse().getRedirectedUrl()).isEqualTo("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
//                + URLEncoder.encode(clientId, StandardCharsets.UTF_8.toString())
//                + "&redirect_uri="
//                + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString()));
//    }
//
//    private String removeNewlines(String input) {
//        if (input == null) {
//            return null;
//        }
//        return input.replaceAll("[\\r\\n]", ""); // 개행 문자 제거
//    }
//}
