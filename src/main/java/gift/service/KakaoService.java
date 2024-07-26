package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.kakao.KakaoMemberResponse;
import gift.controller.kakao.KakaoProperties;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public String sendTokenRequest(String authCode) {

        String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";
        var body = createRequestBody(authCode);

        String tokenResponse =  restClient.post()
                                        .uri(URI.create(tokenRequestUrl))
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        .body(body)
                                        .retrieve()
                                        .body(String.class);

        return extractAccessToken(tokenResponse);
    }

    private MultiValueMap<String, String> createRequestBody(String authCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", authCode);
        return body;
    }

    private String extractAccessToken(String tokenResponse) {
        try {
            JsonNode jsonNode = objectMapper.readTree(tokenResponse);
            return jsonNode.path("access_token").asText();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse access token", e);
        }
    }

    public KakaoMemberResponse getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        var response = restClient.post()
            .uri(URI.create(kakaoProperties.apiUserUri()))
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .retrieve()
            .body(String.class);

        return new KakaoMemberResponse(extractIdFromApiResponse(response));
    }

    private Long extractIdFromApiResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.path("id").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse api response", e);
        }
    }

}
