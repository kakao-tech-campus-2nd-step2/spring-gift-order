package gift;

import gift.config.JwtProvider;
import gift.config.RestTemplateConfig;
import gift.domain.member.Member;
import gift.repository.MemberRepository;
import gift.service.kakao.KakaoLoginService;
import gift.service.kakao.KakaoProperties;
import gift.service.kakao.TokenResponse;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = KakaoLoginService.class)
public class KakaoLoginServiceApiRequestTest {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @MockBean
    private KakaoProperties kakaoProperties;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("인가 코드로 엑세스 토큰을 발급하고, 카카오 유저 정보를 가져와 JWT 토큰을 발행한다.")
    @Test
    void processKakaoAuth() throws Exception {
        //given
        String code = "test-code";
        String expectedTokenResponse = "{ \"access_token\": \"test-access-token\"}";
        String expectedMemberResponse = "{ \"id\": 12345, \"properties\": { \"nickname\": \"test-nickname\" }}";

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(content().string("grant_type=authorization_code&client_id&redirect_uri&code=" + code))
                .andRespond(withSuccess(expectedTokenResponse, MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer test-access-token"))
                .andRespond(withSuccess(expectedMemberResponse, MediaType.APPLICATION_JSON));

        given(memberRepository.findByKakaoId(anyLong())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(new Member());
        given(jwtProvider.create(any(Member.class))).willReturn("test-jwt-token");

        //when
        TokenResponse tokenResponse = kakaoLoginService.processKakaoAuth(code);

        //then
        assertThat(tokenResponse.getAccessToken()).isEqualTo("test-access-token");
        assertThat(tokenResponse.getJwt()).isEqualTo("test-jwt-token");
        mockServer.verify();

        then(memberRepository).should().findByKakaoId(anyLong());
        then(memberRepository).should().save(any(Member.class));
        then(jwtProvider).should().create(any(Member.class));
    }

}
