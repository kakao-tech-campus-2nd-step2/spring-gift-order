package gift.util;


import com.google.gson.Gson;
import gift.dto.OauthTokenDTO;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@PropertySource("classpath:application-dev.properties")
@Component
public class KakaoApiUtil {
    private final RestClient restClient = RestClient.create();

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${client.id}")
    private String clientId;

    private OauthTokenDTO getToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", "http://localhost:8080/login/token");
        body.add("code", code);

        var response = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        return Mapper(response);
    }

    //유저의 정보를 받아오는 메서드
    private ResponseEntity<String> getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        var response = restClient.get()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .toEntity(String.class);


        return response;

    }

    private OauthTokenDTO Mapper(ResponseEntity<String> response) {
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), OauthTokenDTO.class);
    }

    public String getAccessToken(String code) {
        OauthTokenDTO token = getToken(code);
        String accessToken = token.getAccessToken();
        return accessToken;

    }

}
