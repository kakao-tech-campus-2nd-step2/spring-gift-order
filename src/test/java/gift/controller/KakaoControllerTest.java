package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.service.kakao.KakaoLoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class KakaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KakaoLoginService kakaoLoginService;

    @DisplayName("[GET] 카카오톡 로그인 화면으로 Redirect")
    @Test
    void redirectLoginForm() throws Exception {
        //given
        HttpHeaders headers = new HttpHeaders();
        given(kakaoLoginService.getRedirectHeaders()).willReturn(headers);

        //when
        ResultActions result = mvc.perform(get("/kakao/login"));

        //then
        result.andExpect(status().isFound());

        then(kakaoLoginService).should().getRedirectHeaders();
    }

    @DisplayName("[GET] 로그인 후 Redirect 된 URL 처리 - 토큰을 발급하여 응답한다.")
    @Test
    void login() throws Exception {
        //given
        String code = "test_code";
        String accessToken = "test_access_token";

        given(kakaoLoginService.getAccessToken(code)).willReturn(accessToken);

        //when
        ResultActions result = mvc.perform(get("/kakao/oauth")
                .param("code", code));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").value(accessToken));

        then(kakaoLoginService).should().getAccessToken(code);
    }

}
