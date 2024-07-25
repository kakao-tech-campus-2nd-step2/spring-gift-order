package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.oauth2.oAuth2Exception;
import gift.exception.oauth2.oAuth2TokenException;
import gift.response.oauth2.oAuth2MemberInfoResponse;
import gift.response.oauth2.oAuth2TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class KakaoLoginService implements OAuth2LoginService {

    public static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    public static final String MEMBER_INFO_REQUEST_URI = "https://kapi.kakao.com/v2/user/me";
    public static final String AUTH_ERROR = "error";
    public static final String AUTH_ERROR_DESCRIPTION = "error_description";
    private final ObjectMapper mapper;
    private final WebClient client;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoLoginService(ObjectMapper mapper, WebClient client) {
        this.mapper = mapper;
        this.client = client;
    }

    public void checkRedirectUriParams(HttpServletRequest request) {
        if (request.getParameterMap().containsKey(AUTH_ERROR) || request.getParameterMap()
            .containsKey(AUTH_ERROR_DESCRIPTION)) {
            String error = request.getParameter(AUTH_ERROR);
            String errorDescription = request.getParameter(AUTH_ERROR_DESCRIPTION);
            throw new oAuth2Exception(error, errorDescription);
        }
    }

    public oAuth2TokenResponse getToken(String code) {
        try {
            return client.post()
                .uri(URI.create(TOKEN_REQUEST_URI))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(Mono.just(createTokenRequest(clientId, redirectUri, code)),
                    LinkedMultiValueMap.class)
                .retrieve()
                .bodyToMono(oAuth2TokenResponse.class)
                .retry(3)
                .block();
        } catch (WebClientResponseException e) {
            throw new oAuth2TokenException(e);
        }
    }

    public String getMemberInfo(String accessToken) {

        try {
            oAuth2MemberInfoResponse response = client.get()
                .uri(URI.create(MEMBER_INFO_REQUEST_URI))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(oAuth2MemberInfoResponse.class)
                .retry(3)
                .block();

            return Optional.ofNullable(response)
                .map(oAuth2MemberInfoResponse::id)
                .orElseThrow(() -> new oAuth2TokenException("Member ID is null"));

        } catch (WebClientResponseException e) {
            throw new oAuth2TokenException(e);
        }

    }

    public LinkedMultiValueMap<String, String> createTokenRequest(String clientId,
        String redirectUri, String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }

}
