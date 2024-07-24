package gift.auth;

import gift.auth.dto.KakaoAccessToken;
import gift.auth.dto.KakaoProperties;
import java.net.URI;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final static String KAKAO_URL = "https://kauth.kakao.com/oauth/token";
    private final RestClient client;
    private final KakaoProperties properties;

    public KakaoClient(KakaoProperties properties) {
        this.client = RestClient.builder().build();
        this.properties = properties;
    }

    public String getAccessToken(String authorizationCode){
        var body = properties.toBody(authorizationCode);

        ResponseEntity<KakaoAccessToken> response = client.post()
            .uri(URI.create(KAKAO_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessToken.class);

        return Objects.requireNonNull(response.getBody(), "reponse가 null 값입니다.").accessToken();
    }

    public KakaoProperties getProperties(){
        return properties;
    }
}
