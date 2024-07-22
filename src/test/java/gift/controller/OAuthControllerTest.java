package gift.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class OAuthControllerTest {
    @InjectMocks
    private OAuthController oAuthController;
    @Mock
    private KakaoProperties kakaoProperties;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getAcessToken 메서드 성공 테스트")
    public void testGetAccessToken() throws Exception {
        when(kakaoProperties.clientId()).thenReturn("testClientId");
        when(kakaoProperties.redirectUrl()).thenReturn("http://localhost");

        String responseBody = "{\"scope\":\"talk_message\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.exchange(any(), eq(String.class))).thenReturn(responseEntity);

        JsonNode jsonNode = Mockito.mock(JsonNode.class);
        when(jsonNode.get("scope")).thenReturn(Mockito.mock(JsonNode.class));
        when(jsonNode.get("scope").asText()).thenReturn("talk_message");
        when(objectMapper.readTree(responseBody)).thenReturn(jsonNode);

        ResponseEntity<String> response = oAuthController.getAccessToken("testCode");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    @DisplayName("getAccessToken 메서드 실패 테스트")
    public void testGetAccessToken_NoTalkMessageScope() throws Exception {
        when(kakaoProperties.clientId()).thenReturn("testClientId");
        when(kakaoProperties.redirectUrl()).thenReturn("http://localhost");

        String responseBody = "{\"scope\":\"other_scope\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.exchange(any(), eq(String.class))).thenReturn(responseEntity);

        JsonNode jsonNode = Mockito.mock(JsonNode.class);
        when(jsonNode.get("scope")).thenReturn(Mockito.mock(JsonNode.class));
        when(jsonNode.get("scope").asText()).thenReturn("other_scope");
        when(objectMapper.readTree(responseBody)).thenReturn(jsonNode);

        ResponseEntity<String> response = oAuthController.getAccessToken("testCode");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("[spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.", response.getBody());
    }
}
