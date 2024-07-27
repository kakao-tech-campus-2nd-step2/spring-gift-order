package gift;

import gift.service.KakaoService;
import gift.exception.MemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private KakaoService kakaoService;

    @Test
    public void testLogin() {
        String url = "http://localhost:" + port + "/kakao/login";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCallbackKakaoSuccess() {
        String code = "valid_code";
        String accessToken = "valid_access_token";

        when(kakaoService.login(code)).thenReturn(accessToken);

        String url = "http://localhost:" + port + "/kakao/oauth2/callback?code=" + code;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testCallbackKakaoMemberNotFound() {
        String code = "valid_code";

        when(kakaoService.login(code)).thenThrow(MemberNotFoundException.class); // 메시지 없이 예외 타입만 설정

        String url = "http://localhost:" + port + "/kakao/oauth2/callback?code=" + code;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("register");
    }
}
