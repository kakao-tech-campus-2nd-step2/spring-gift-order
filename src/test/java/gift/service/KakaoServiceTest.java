package gift.service;

import gift.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KakaoServiceTest {

    @Mock(lenient = true)
    private RestTemplate kakaoRestTemplate;

    @Mock(lenient = true)
    private KakaoProperties kakaoProperties;

    @InjectMocks
    private KakaoService kakaoService;

    @BeforeEach
    void setUp() {
        // These stubs are now lenient, so Mockito won't complain about them being unused
        when(kakaoProperties.getClientId()).thenReturn("client-id");
        when(kakaoProperties.getRedirectUri()).thenReturn("redirect-uri");
        when(kakaoProperties.getApiKey()).thenReturn("api-key");
        when(kakaoProperties.getApiUrl()).thenReturn("api-url");
    }

    @Test
    void testSendKakaoMessage_nullResponse() {
        // Given
        OrderResponse orderResponse = new OrderResponse.Builder()
                .id(1L)
                .optionId(1L)
                .quantity(2)
                .orderDateTime(LocalDateTime.now())
                .message("Test Message")
                .build();

        when(kakaoRestTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(null);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            kakaoService.sendKakaoMessage(orderResponse);
        });

        assertThat(exception.getMessage()).contains("카카오 메시지 전송에 실패했습니다.: null response");
    }

    @Test
    void testGetAccessToken() {
        // Given
        String code = "test-code";

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(Map.of("access_token", "test-access-token"), HttpStatus.OK);

        when(kakaoRestTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        String accessToken = kakaoService.getAccessToken(code);

        // Then
        assertThat(accessToken).isEqualTo("test-access-token");
    }

    @Test
    void testGetAccessToken_nullResponse() {
        // Given
        String code = "test-code";

        when(kakaoRestTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(null);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            kakaoService.getAccessToken(code);
        });

        assertThat(exception.getMessage()).contains("엑세스 토큰을 받을 수 없습니다.");
    }
}