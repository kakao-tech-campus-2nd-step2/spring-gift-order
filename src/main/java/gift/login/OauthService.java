package gift.login;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.exception.FailedLoginException;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OauthService {

    private final RestClient restClient = RestClient.create();

    private final KakaoOauthConfigure kakaoOauthConfigure;

    public OauthService(KakaoOauthConfigure kakaoOauthConfigure) {
        this.kakaoOauthConfigure = kakaoOauthConfigure;
    }

    public URI loginKakao() {
        ResponseEntity<String> response = restClient.get()
            .uri(generateKakaoLoginURL())
            .retrieve()
            .toEntity(String.class);

        return URI.create(String.valueOf(response.getHeaders().getLocation()));
    }

    private String generateKakaoLoginURL() {
        return UriComponentsBuilder.fromHttpUrl(kakaoOauthConfigure.getAuthorizeCodeURL())
            .queryParam("client_id", kakaoOauthConfigure.getCliendId())
            .queryParam("redirect_uri", kakaoOauthConfigure.getRedirectURL())
            .queryParam("response_type", "code").toUriString();
    }

    public String getTokenFromKakao(String code) {
        KakaoTokenResponseDTO response = restClient.post()
            .uri(kakaoOauthConfigure.getTokenURL())
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(generateBodyToKakao(code))
            .retrieve()
            .toEntity(KakaoTokenResponseDTO.class)
            .getBody();

        if (response == null) {
            throw new FailedLoginException("Kakao token not found");
        }

        return response.getAccessToken();
    }

    private LinkedMultiValueMap<String, String> generateBodyToKakao(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfigure.getCliendId());
        body.add("redirect_uri", kakaoOauthConfigure.getRedirectURL());
        return body;
    }
}