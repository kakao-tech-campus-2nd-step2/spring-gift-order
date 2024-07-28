package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.kakao.KakaoMemberInfo;
import gift.controller.kakao.KakaoProperties;
import java.io.IOException;
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

        var body = createRequestBody(authCode);
        String tokenResponse =  restClient.post()
                                        .uri(kakaoProperties.getTokenUri())
                                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                        .body(body)
                                        .retrieve()
                                        .body(String.class);

        return extractAccessToken(tokenResponse);
    }

    private MultiValueMap<String, String> createRequestBody(String authCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri().toString());
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

    public KakaoMemberInfo getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        body.add("property_keys", "[\"kakao_account.email\"]");
        var response = restClient.post()
            .uri(kakaoProperties.getUserUri())
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .body(body)
            .retrieve()
            .toEntity(KakaoMemberInfo.class);

        return response.getBody();
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
