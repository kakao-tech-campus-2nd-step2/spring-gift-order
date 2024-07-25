package gift.e2e;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.service.KakaoUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
@AutoConfigureMockMvc //패키지의 모든 테스트 수행 중 오류방지를 위해 추가
class KakaoE2EWithoutMockTest {

    private final String testCode = "testCode";

    @Autowired
    private KakaoUserService kakaoUserService;
    @Test
    @DisplayName("accessToken 오류 테스트")
    void accessTokenErrorTest() {
        assertThatThrownBy(() -> kakaoUserService.getAccessToken(testCode))
            .isInstanceOf(HttpClientErrorException.class)
            .hasMessage(
                "400 Bad Request: \"{\"error\":\"invalid_grant\",\"error_description\":\"authorization code not found for code="
                    + testCode + "\",\"error_code\":\"KOE320\"}\"");
    }

}
