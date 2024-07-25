package gift.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class KakaoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KakaoService kakaoService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetAccessTokenSuccess() {
        // Given
        String code = "test-code";
        String accessToken = "test-access-token";
        String url = "https://kauth.kakao.com/oauth/token";

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.OK);

        when(restTemplate.exchange(any(RequestEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        // When
        String result = kakaoService.getAccessToken(code);

        // Then
        assertThat(result).isEqualTo(accessToken);
    }

    @Test
    void testGetAccessTokenFailure() {
        // Given
        String code = "test-code";
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "invalid_grant");

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, headers, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(any(RequestEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kakaoService.getAccessToken(code);
        });
        assertThat(exception.getMessage()).contains("액세스 토큰 요청에 실패했습니다.");
    }
}
