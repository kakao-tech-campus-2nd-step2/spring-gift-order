package gift.service;

import gift.service.kakao.KakaoLoginService;
import gift.service.kakao.KakaoProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class KakaoLoginServiceTest {

    @Mock
    private KakaoProperties properties;

    @Mock
    private RestClient client;

    @InjectMocks
    private KakaoLoginService kakaoLoginService;

    @DisplayName("Redirect URL 을 header location 에 넣고 헤더를 반환한다.")
    @Test
    void getRedirectHeaders() throws Exception {
        //given
        String redirectUrl = "https://test.com";
        String clientId = "test-client-id";

        given(properties.redirectUrl()).willReturn(redirectUrl);
        given(properties.clientId()).willReturn(clientId);

        //when
        HttpHeaders headers = kakaoLoginService.getRedirectHeaders();

        //then
        String expectedUrl = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
                + "&redirect_uri=" + redirectUrl
                + "&client_id=" + clientId;

        assertThat(headers.getLocation().toString()).isEqualTo(expectedUrl);
        then(properties).should().redirectUrl();
        then(properties).should().clientId();
    }

//    @DisplayName("Access Token 을 가져온다.")
//    @Test
//    void getAccessToken() throws Exception {
//        //given
//        String code = "auth-code";
//        String clientId = "test-client-id";
//        String redirectUrl = "https://test.com";
//        String accessToken = "test-access-token";
//
//        KakaoTokenInfoResponse response = new KakaoTokenInfoResponse(accessToken);
//
//        given(properties.clientId()).willReturn(clientId);
//        given(properties.redirectUrl()).willReturn(redirectUrl);
//
//        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
//        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
//        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
//
//        given(client.post()).willReturn(requestBodyUriSpec);
//        given(requestBodyUriSpec.uri(anyString())).willReturn(requestBodyUriSpec);
//        given(requestBodyUriSpec.contentType(any(MediaType.class))).willReturn(requestBodyUriSpec);
//        given(requestBodyUriSpec.body(any())).willReturn(requestBodySpec);
//        given(requestBodySpec.retrieve()).willReturn(responseSpec);
//        given(responseSpec.body(KakaoTokenInfoResponse.class)).willReturn(response);
//
//
//        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", clientId);
//        body.add("redirect_uri", redirectUrl);
//        body.add("code", code);
//
//        given(client.post()
//                .uri("https://kauth.kakao.com/oauth/token")
//                .contentType(APPLICATION_FORM_URLENCODED)
//                .body(body)
//                .retrieve()
//                .body(KakaoTokenInfoResponse.class)).willReturn(response);
//
//        //when
//        String actualAccessToken = kakaoLoginService.getAccessToken(code);
//
//        //then
//        assertThat(actualAccessToken).isEqualTo(accessToken);
//        then(properties).should().clientId();
//        then(properties).should().redirectUrl();
//    }

}
