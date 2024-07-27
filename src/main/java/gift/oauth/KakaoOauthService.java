package gift.oauth;

import static gift.exception.ErrorMessage.KAKAO_AUTHENTICATION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.exception.FailedLoginException;
import gift.member.MemberService;
import java.net.URI;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoOauthService {

    private final RestClient restClient;
    private final MemberService memberService;
    private final KakaoOauthConfigure kakaoOauthConfigure;

    public KakaoOauthService(
        KakaoOauthConfigure kakaoOauthConfigure,
        RestClient restClient,
        MemberService memberService
    ) {
        this.kakaoOauthConfigure = kakaoOauthConfigure;
        this.restClient = restClient;
        this.memberService = memberService;
    }

    public URI getLoginURL() {
        return UriComponentsBuilder.fromHttpUrl(kakaoOauthConfigure.getAuthorizeCodeURL())
            .queryParam("client_id", kakaoOauthConfigure.getClientId())
            .queryParam("redirect_uri", kakaoOauthConfigure.getRedirectURL())
            .queryParam("response_type", "code")
            .build()
            .toUri();
    }

    public String getToken(String code) {
        KakaoTokenResponseDTO kakaoToken = restClient.post()
            .uri(kakaoOauthConfigure.getTokenURL())
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(generateBody(code))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                (request, response) -> {
                    throw new FailedLoginException(KAKAO_AUTHENTICATION_FAILED);
                }
            ).toEntity(KakaoTokenResponseDTO.class)
            .getBody();

        memberService.registerIfNotExistsByIdToken(Objects.requireNonNull(kakaoToken).getIdToken());

        return Objects.requireNonNull(kakaoToken).getAccessToken();
    }

    private LinkedMultiValueMap<String, String> generateBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOauthConfigure.getClientId());
        body.add("redirect_uri", kakaoOauthConfigure.getRedirectURL());
        return body;
    }

}