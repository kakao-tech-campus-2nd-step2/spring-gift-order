package gift.utils;

import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.KakaoApiDTO.KakaoCode;
import gift.utils.config.KakaoProperties;
import java.util.Collections;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalApiService {
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final String baseUrl = "https://kauth.kakao.com/oauth";

    public ExternalApiService(RestTemplateBuilder restTemplateBuilder, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplateBuilder.build();
        this.kakaoProperties = kakaoProperties;
    }


    public ResponseEntity<?> getKakaoCode(String code) {
        String endpoint = "/authorize";
        String fullUrl = baseUrl + endpoint;

        try {
            String uriString = UriComponentsBuilder.fromHttpUrl(fullUrl)
                .queryParam("response_type", code)
                .queryParam("client_id", kakaoProperties.getRestApiKey())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                .toUriString();

            ResponseEntity<?> response = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                null,
                Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
