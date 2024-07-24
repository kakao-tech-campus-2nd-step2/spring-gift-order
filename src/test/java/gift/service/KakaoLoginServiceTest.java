package gift.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;


class KakaoLoginServiceTest {

//    KakaoLoginService kakaoLoginService;
//
//    @Test
//    public void testGetToken() {
//        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(RestClient.builder()).build();
//
//        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
//                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
//
//        KakaoTokenResponse token = kakaoLoginService.getToken("testCode");
//        System.out.println(token);
//        //                    new KakaoTokenResponse("wef", "ewf", "ew", 123L, "213", 23L)
////        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
////                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
//
////        kakaoLoginService.getToken("testToken");
//
//        // Arrange
////        String code = "test_code";
////        KakaoTokenResponse mockResponse = new KakaoTokenResponse("tokenType", "scope", "accessToken", 1010L, "refreshToken", 8080L);
////        RestClient.ResponseSpec spec = Res
////        when(RestClient.create().post()
////                .uri(any(String.class))
////                .body(any(MultiValueMap.class))
////                .retrieve())
////                .thenReturn()
////                .body(KakaoTokenResponse.class))
////                .thenReturn(mockResponse);
//
//        // Act
////        KakaoTokenResponse response = kakaoLoginService.getToken("code");
//
//        // Assert
////        assertNotNull(response);
////        verify(client.post(), times(1)).uri(TOKEN_REQUEST_URI);
////        verify(client.post(), times(1)).body(any(MultiValueMap.class));
//    }
}