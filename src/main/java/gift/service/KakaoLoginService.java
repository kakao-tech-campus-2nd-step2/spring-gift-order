package gift.service;

import gift.domain.KakaoTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatusCode;

import java.net.URI;

@Service
public class KakaoLoginService {
    private String clientId;
    private String redirectUri;
    private String clientSecret;

    private final RestClient client = RestClient.builder().build();

    public KakaoLoginService(@Value("${kakao.client_id}") String clientId,
                             @Value("${kakao.redirect_url}") String redirectUri,
                             @Value("${kakao.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    public LinkedMultiValueMap<String, String> getBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);
        return body;
    }

    public String getToken(String code) {
        KakaoTokenResponseDTO kakaoTokenResponseDTO = client.post()
                .uri(URI.create("https://kauth.kakao.com/oauth/token"))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(getBody(code))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("잘못된 토큰 요청입니다.");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("카카오 서버 오류입니다.");
                })
                .body(KakaoTokenResponseDTO.class);

        return kakaoTokenResponseDTO.getAccessToken();
    }
}
