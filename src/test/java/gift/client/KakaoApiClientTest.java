package gift.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.client.requestBody.KakaoTokenRequestBody;
import gift.dto.response.KakaoTokenResponse;
import gift.exception.KakaoApiHasProblemException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(KakaoApiClient.class)
@DisplayName("카카오 RestClient Test")
class KakaoApiClientTest {

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @Autowired
    private KakaoApiClient kakaoApiClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockRestServiceServer server;

    @Test
    @DisplayName("카카오 액세스 토큰 받기 - 성공")
    void getKakaoToken() throws JsonProcessingException {
        //Given
        KakaoTokenRequestBody body = new KakaoTokenRequestBody("code");
        KakaoTokenResponse response = new KakaoTokenResponse("type", "scope", "access", 100L, "refersh", 1000L);

        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

        //When
        KakaoTokenResponse token = kakaoApiClient.getKakaoToken(body);

        //Then
        assertThat(token).isInstanceOf(KakaoTokenResponse.class);
        assertThat(token.accessToken()).isEqualTo("access");
    }

    @Test
    @DisplayName("카카오 액세스 토큰 받기 - 실패")
    void getKakaoToken2() {
        //Given
        KakaoTokenRequestBody body = new KakaoTokenRequestBody("code");

        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());

        //When Then
        assertThatThrownBy(() -> kakaoApiClient.getKakaoToken(body)).
                isInstanceOf(KakaoApiHasProblemException.class);
    }

    @Test
    void getMemberEmail() {
    }

    @Test
    void sendMessageToMe() {
    }
}
