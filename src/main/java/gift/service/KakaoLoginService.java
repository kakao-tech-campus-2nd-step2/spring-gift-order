package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.kakao.KakaoAuthException;
import gift.response.KakaoTokenResponse;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    public static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    public static final String AUTH_ERROR = "error";
    public static final String AUTH_ERROR_DESCRIPTION = "error_description";
    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper mapper;

    public KakaoLoginService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void checkRedirectUriParams(MultiValueMap<String, Object> params) {
        if (params.containsKey(AUTH_ERROR) || params.containsKey(AUTH_ERROR_DESCRIPTION)) {
            String error = (String) params.getFirst(AUTH_ERROR);
            String errorDescription = (String) params.getFirst(AUTH_ERROR_DESCRIPTION);
            throw new KakaoAuthException(error, errorDescription);
        }
    }

    public KakaoTokenResponse getToken(String clientId, String redirectUri, String code)
        throws JsonProcessingException {
        ResponseEntity<String> tokenResponse = client.post()
            .uri(URI.create(TOKEN_REQUEST_URI))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(createTokenRequest(clientId, redirectUri, code))
            .retrieve()
            .toEntity(String.class);

        return mapper.readValue(tokenResponse.getBody(),
            KakaoTokenResponse.class);
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
