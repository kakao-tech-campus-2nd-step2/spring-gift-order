package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.response.KakaoTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(KakaoLoginService.class)
@DisplayName("카카오 로그인 서비스 테스트")
class KakaoLoginServiceTest {

    @Autowired
    KakaoLoginService kakaoLoginService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockRestServiceServer server;
    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("파싱이 제대로 되는지 테스트")
    void getToken() throws JsonProcessingException {
        //Given
        KakaoTokenResponse kakaoTokenResponse = new KakaoTokenResponse("type", "scope", "access", 100L, "refresh", 1000L);

        //When
        server.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(kakaoTokenResponse), MediaType.APPLICATION_JSON));

        KakaoTokenResponse token = kakaoLoginService.getToken("testCode");

        //Then
        assertThat(token).isInstanceOf(KakaoTokenResponse.class);
        assertThat(token.access_token()).isEqualTo("access");
    }
}