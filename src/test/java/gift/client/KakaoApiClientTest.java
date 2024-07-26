package gift.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
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
        MultiValueMap<String, String> bodyParams = mock(MultiValueMap.class);

        KakaoTokenResponse response = new KakaoTokenResponse("type", "scope", "access", 100L, "refersh", 1000L);

        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

        //When
        KakaoTokenResponse token = kakaoApiClient.getKakaoToken(bodyParams);

        //Then
        assertThat(token).isInstanceOf(KakaoTokenResponse.class);
        assertThat(token.accessToken()).isEqualTo("access");
    }

    @Test
    @DisplayName("카카오 액세스 토큰 받기 - 실패")
    void getKakaoToken2() {
        //Given
        MultiValueMap<String, String> bodyParams = mock(MultiValueMap.class);


        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(MockRestResponseCreators.withGatewayTimeout());

        //When Then
        assertThatThrownBy(() -> kakaoApiClient.getKakaoToken(bodyParams)).
                isInstanceOf(KakaoApiHasProblemException.class);
    }

    @Test
    void getMemberEmail() {
    }

    @Test
    void sendMessageToMe() {
    }
}
