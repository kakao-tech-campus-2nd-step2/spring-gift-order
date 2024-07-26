package gift.service;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.kakao.KakaoMemberResponse;
import gift.controller.kakao.KakaoProperties;
import java.net.URI;
import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Service;
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
