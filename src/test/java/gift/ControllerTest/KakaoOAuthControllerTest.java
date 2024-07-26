package gift.ControllerTest;

import gift.Controller.KakaoOAuthController;
import gift.Service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KakaoOAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testAuthorize() throws Exception {
        mockMvc.perform(get("/oauth/authorize"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testCallBack() throws Exception {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        // 인가 코드로 액세스 토큰을 요청하는 부분
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().string(containsString("code=mock-code")))
                .andRespond(withSuccess("{\"access_token\": \"valid-access-token\", \"token_type\": \"bearer\"}", MediaType.APPLICATION_JSON));

        // 사용자 정보 요청
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(org.springframework.http.HttpMethod.GET))
                .andRespond(withSuccess("{\"id\": 12345}", MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/auth/kakao/callback").param("code", "mock-code"))
                .andExpect(status().isSeeOther()) // 3xx인지 확인
                .andExpect(header().string("Location", "/products")) // 리다이렉트 주소 확인
                .andExpect(cookie().exists("accessToken")) // accessToken 쿠키가 있는지 확인
                .andExpect(request().sessionAttribute("kakaoId", is(12345L))) // 세션에 kakaoId가 12345인지 확인
                .andExpect(content().string(containsString("Successfully logged in"))); // 성공 메시지가 있는지 확인

        mockServer.verify();
    }
}
