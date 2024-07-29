//package gift.service;
//
//import gift.entity.Properties;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class ExternalAPIServiceTest {
//
//    @InjectMocks
//    private ExternalAPIService externalAPIService;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @Mock
//    private Properties properties;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        when(properties.getClientId()).thenReturn("testClientId");
//        when(properties.getRedirectUri()).thenReturn("http://localhost/callback");
//
//        ReflectionTestUtils.setField(externalAPIService, "client", restTemplate);
//    }
//
//    @Test
//    public void testHandleKakaoRedirect() {
//        String location = "http://localhost/callback?code=testCode&state=testState";
//
//        ExternalAPIService spyExternalAPIService = spy(externalAPIService);
//        doNothing().when(spyExternalAPIService).getKakaoToken(anyString());
//
//        spyExternalAPIService.handleKakaoRedirect(location);
//
//        verify(spyExternalAPIService, times(1)).getKakaoToken("testCode");
//    }
//
//    @Test
//    public void testGetKakaoAuthorize() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("http://localhost/callback?code=testCode&state=testState"));
//        ResponseEntity<String> responseEntity = new ResponseEntity<>(headers, HttpStatus.FOUND);
//
//        when(restTemplate.exchange(any(RequestEntity.class), eq(String.class))).thenReturn(responseEntity);
//
//        ExternalAPIService spyExternalAPIService = spy(externalAPIService);
//        doNothing().when(spyExternalAPIService).handleKakaoRedirect(anyString());
//
//        spyExternalAPIService.getKakaoAuthorize();
//
//        ArgumentCaptor<RequestEntity> requestCaptor = ArgumentCaptor.forClass(RequestEntity.class);
//        verify(restTemplate, times(1)).exchange(requestCaptor.capture(), eq(String.class));
//
//        RequestEntity request = requestCaptor.getValue();
//        assertEquals(HttpMethod.GET, request.getMethod());
//        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, request.getHeaders().getContentType());
//
//        verify(spyExternalAPIService, times(1)).handleKakaoRedirect(anyString());
//    }
//
//    @Test
//    public void testGetKakaoToken() {
//
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("mockTokenResponse", HttpStatus.OK);
//
//        when(restTemplate.exchange(any(RequestEntity.class), eq(String.class))).thenReturn(responseEntity);
//
//        externalAPIService.getKakaoToken("testCode");
//
//        ArgumentCaptor<RequestEntity> requestCaptor = ArgumentCaptor.forClass(RequestEntity.class);
//        verify(restTemplate, times(1)).exchange(requestCaptor.capture(), eq(String.class));
//
//        RequestEntity request = requestCaptor.getValue();
//        assertEquals(HttpMethod.POST, request.getMethod());
//        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, request.getHeaders().getContentType());
//
//        LinkedMultiValueMap<String, String> body = (LinkedMultiValueMap<String, String>) request.getBody();
//        assertNotNull(body);
//        assertEquals("authorization_code", body.getFirst("grant_type"));
//        assertEquals("testClientId", body.getFirst("client_id"));
//        assertEquals("http://localhost/callback", body.getFirst("redirect_uri"));
//        assertEquals("testCode", body.getFirst("code"));
//    }
//}
