package gift.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.oauth.dto.KakaoTokenFailResponse;
import gift.oauth.dto.KakaoTokenResponse;
import gift.oauth.exception.KakaoOAuthException;
import gift.oauth.properties.KakaoProperties;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional(readOnly = true)
public class OAuthService {

    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public OAuthService(KakaoProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.client = RestClient.builder().build();
    }

    public String getAuthorizeUri(){
        return UriComponentsBuilder.newInstance()
            .path("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type","code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUrl())
            .toUriString();
    }

    public String getAccessToken(String code){

        String url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = creatBody(code);

         KakaoTokenResponse tokenResponse = client.post()
             .uri(URI.create(url))
             .contentType(MediaType.APPLICATION_FORM_URLENCODED)
             .body(body)
             .exchange((request, response) -> {
                 if (response.getStatusCode().is4xxClientError()) {
                     KakaoTokenFailResponse failResponse = objectMapper.readValue(response.getBody(), KakaoTokenFailResponse.class);
                     throw new KakaoOAuthException(failResponse.errorCode(), failResponse.errorDescription());
                 }

                 return objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
             });

         return tokenResponse.accessToken();
    }

    private LinkedMultiValueMap<String, String> creatBody(String code) {

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}

