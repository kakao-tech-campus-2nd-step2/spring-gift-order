package gift.service.kakaoAuth;

import gift.web.dto.Token;
import java.net.URI;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;

    public KakaoAuthService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getKakaoAuthUrl() {
        StringBuffer str = new StringBuffer();
        str.append("https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code");
        str.append("&redirect_uri=" + kakaoProperties.redirectUri());
        str.append("&client_id=" + kakaoProperties.clientId());

        return str.toString();
    }

    public Token receiveToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        var body = kakaoProperties.createBody(code);

        RestClient client = RestClient.create();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(Map.class);

        //System.out.println(response);
        //System.out.println(response.getBody().get("access_token"));
        return new Token(response.getBody().get("access_token").toString());
    }

    public KakaoInfo getMemberInfoFromKakaoServer(Token accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        RestClient client = RestClient.create();
        ResponseEntity<Map<String, Object>> response = client.post()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken.token())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, String> responseBody = (Map<String, String>) response.getBody().get("kakao_account");

        return new KakaoInfo(responseBody.get("email"));
    }
}
