package gift.auth.oauth.kakao;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.auth.dto.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class KakaoLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoLoginService kakaoLoginService;


    @Test
    @DisplayName("카카오 로그인 컨트롤러 테스트")
    void login() throws Exception {
        // given
        given(kakaoLoginService.login(anyString())).willReturn(new Token("testToken"));

        // when & then
        mockMvc.perform(get("/oauth/kakao/login")
            .param("code", "testCode"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("testToken"));
    }
}