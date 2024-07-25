package gift.login;

import static gift.exception.ErrorMessage.KAKAO_AUTHENTICATION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import java.net.URI;
import java.util.Objects;
import javax.security.sasl.AuthenticationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OauthService {

    private final RestClient restClient;

    private final KakaoOauthConfigure kakaoOauthConfigure;

    public OauthService(
        KakaoOauthConfigure kakaoOauthConfigure,
        RestClient restClient
    ) {
        this.kakaoOauthConfigure = kakaoOauthConfigure;
        this.restClient = restClient;
    }

    public URI loginKakao() {
        ResponseEntity<String> kakaoLoginPage = restClient.get()
            .uri(generateKakaoLoginURL())
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                (request, response) -> {
                    throw new AuthenticationException(KAKAO_AUTHENTICATION_FAILED);
                }
            ).toEntity(String.class);

        return kakaoLoginPage.getHeaders().getLocation();
    }

    private String generateKakaoLoginURL() {
        return UriComponentsBuilder.fromHttpUrl(kakaoOauthConfigure.getAuthorizeCodeURL())
            .queryParam("client_id", kakaoOauthConfigure.getClientId())
            .queryParam("redirect_uri", kakaoOauthConfigure.getRedirectURL())
            .queryParam("response_type", "code")
            .toUriString();
    }

    public String getTokenFromKakao(String code) {
        KakaoTokenResponseDTO kakaoToken = restClient.post()
            .uri(kakaoOauthConfigure.getTokenURL())
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(generateBodyToKakao(code))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                (request, response) -> {
                    throw new AuthenticationException(KAKAO_AUTHENTICATION_FAILED);
                }
            ).toEntity(KakaoTokenResponseDTO.class)
            .getBody();

        return Objects.requireNonNull(kakaoToken).getAccessToken();
    }

    private LinkedMultiValueMap<String, String> generateBodyToKakao(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfigure.getClientId());
        body.add("redirect_uri", kakaoOauthConfigure.getRedirectURL());
        return body;
    }
}