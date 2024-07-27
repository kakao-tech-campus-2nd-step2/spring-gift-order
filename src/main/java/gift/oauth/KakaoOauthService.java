package gift.oauth;

import static gift.exception.ErrorMessage.KAKAO_AUTHENTICATION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.exception.FailedLoginException;
import gift.exception.InvalidAccessTokenException;
import gift.member.MemberService;
import gift.oauth.config.KakaoOauthConfigure;
import gift.oauth.message.DefaultMessageTemplate;
import gift.oauth.dto.KakaoTokenResponseDTO;
import gift.oauth.dto.KakaoUserInfoDTO;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoOauthService {

    private final RestClient restClient;
    private final KakaoOauthConfigure kakaoOauthConfigure;
    private final MemberService memberService;

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

        Pair<String, Long> emailAndPassword = getEmailAndSubFromAccessToken(
            Objects.requireNonNull(kakaoToken).getAccessToken()
        );

        memberService.registerIfNotExists(
            emailAndPassword.getFirst(),
            emailAndPassword.getSecond().toString()
        );

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

    public void sendMessage(String message, String accessToken) {
        restClient.post()
            .uri(kakaoOauthConfigure.getMessageSendURL())
            .header("Authorization", accessToken)
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(generateMessageBody(message))
            .exchange((request, response) -> {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new InvalidAccessTokenException(KAKAO_AUTHENTICATION_FAILED);
                }
                return ResponseEntity.ok();
            });
    }

    private LinkedMultiValueMap<String, String> generateMessageBody(String message) {
        DefaultMessageTemplate defaultMessageTemplate = new DefaultMessageTemplate(
            "text", message, Map.of(), "버튼"
        );

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", defaultMessageTemplate.toJson());

        return body;
    }

    public Pair<String, Long> getEmailAndSubFromAccessToken(String accessToken) {
        if (!accessToken.startsWith("Bearer")) {
            accessToken = "Bearer " + accessToken;
        }

        return restClient.get()
            .uri(kakaoOauthConfigure.getUserInfoFromAccessTokenURL())
            .header("Authorization", accessToken)
            .accept(APPLICATION_FORM_URLENCODED)
            .exchange((request, response) -> {
                if (response.getStatusCode().is2xxSuccessful()) {
                    KakaoUserInfoDTO kakaoUserInfoDTO = response.bodyTo(KakaoUserInfoDTO.class);
                    return Pair.of(
                        Objects.requireNonNull(kakaoUserInfoDTO).getEmail(),
                        kakaoUserInfoDTO.getId()
                    );
                }

                throw new InvalidAccessTokenException(KAKAO_AUTHENTICATION_FAILED);
            });
    }
}