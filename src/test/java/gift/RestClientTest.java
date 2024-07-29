package gift;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @DisplayName("카카오 API 토큰 요청")
    @Test
    void kakaoApiRequestTokenTest(){
        var url = "https://kauth/kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", "d10bca9343a675e1c7e772e899667311");
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", "uiYWz9LbrO0csCQZWGVfibkQCmapmRyw72S2BznCWhT58KHPV5ukDQAAAAQKPCQfAAABkP4_naDokopMIboAuA");

        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
