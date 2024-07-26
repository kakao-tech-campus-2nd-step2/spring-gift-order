package gift.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class KakaoServiceTest {

    @Mock
    private RestTemplate kakaoRestTemplate;

    @InjectMocks
    private KakaoService kakaoService;

    @Value("${kakao.client-id}")
    private String clientId = "test-client-id";

    @Value("${kakao.redirect-uri}")
    private String redirectUri = "http://localhost:8080/callback";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAccessToken_Success() {
        String code = "test-code";
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", "test-access-token");

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(kakaoRestTemplate.exchange(any(RequestEntity.class), eq(Map.class))).thenReturn(responseEntity);

        String accessToken = kakaoService.getAccessToken(code);

        assertNotNull(accessToken);
        assertEquals("test-access-token", accessToken);
    }

    @Test
    public void testGetAccessToken_Failure() {
        String code = "test-code";
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(kakaoRestTemplate.exchange(any(RequestEntity.class), eq(Map.class))).thenReturn(responseEntity);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            kakaoService.getAccessToken(code);
        });

        assertEquals("액세스 토큰 요청에 실패했습니다.: null", exception.getMessage());
    }
}