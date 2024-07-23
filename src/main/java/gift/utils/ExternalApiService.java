package gift.utils;

import gift.controller.dto.KakaoTokenDto;
import gift.utils.config.KakaoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final String baseUrl = "https://kauth.kakao.com/oauth";

    public ExternalApiService(RestTemplateBuilder restTemplateBuilder, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplateBuilder.build();
        this.kakaoProperties = kakaoProperties;
    }


    public ResponseEntity<KakaoTokenDto> postKakaoToken(String code) {
        String endpoint = "/token";
        UriComponentsBuilder fullUrl = UriComponentsBuilder.fromHttpUrl(baseUrl + endpoint);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type", "authorization_code");
            requestBody.add("client_id", kakaoProperties.getRestApiKey());
            requestBody.add("redirect_uri", kakaoProperties.getRedirectUri());
            requestBody.add("code", code);
//            client_secret이
//            if (kakaoProperties.getClientSecret() != null && !kakaoProperties.getClientSecret().isEmpty()) {
//                params.add("client_secret", kakaoProperties.getClientSecret());
//            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody,
                headers);

        try {
            ResponseEntity<KakaoTokenDto> response = restTemplate.exchange(
                fullUrl.toUriString(),
                HttpMethod.POST,
                requestEntity,
                KakaoTokenDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (HttpClientErrorException e) {
        // 400번대 에러
        logger.error("클라이언트 에러: {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(null);
    } catch (HttpServerErrorException e) {
        // 500번대 에러
        logger.error("카카오 API 서버 에러: {}", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(null);
    } catch (ResourceAccessException e) {
        // 네트워크 에러
        logger.error("카카오 API 네트워크 에러: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    } catch (RestClientException e) {
        // RestTemplate 에러
        logger.error("RestTemlpate 에러: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    }


}
