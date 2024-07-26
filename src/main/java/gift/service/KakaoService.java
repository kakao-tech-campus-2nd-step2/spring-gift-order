package gift.service;

import gift.common.config.KakaoProperties;
import gift.kakaologin.KakaoResponse;
import gift.model.order.Order;
import gift.model.order.OrderRequest;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

//    @Value("${kakao.client-id}")
//    private String restKey;
//
//    @Value("${kakao.redirect-url}")
//    private  String redirectUrl;

    @Autowired
    private KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();

    public String getKakaoUrl() {
        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + kakaoProperties.clientId() +
                "&redirect_uri=" + kakaoProperties.redirectUrl() +
                "&response_type=code";
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody(code);
        var response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, responses) -> {
                throw new RuntimeException("4xx error");
        })
            .onStatus(HttpStatusCode::is5xxServerError, (request, responses) -> {
                throw new RuntimeException("5xx error");
            })
            .toEntity(KakaoResponse.class);
        return response.getBody().getAccessToken();
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", code);
        return body;
    }


}
