package gift.service;

import gift.dto.KakaoUserInfo;
import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String KAKAO_API_URI = "https://kapi.kakao.com";

    public KakaoUserInfo getUserInfo(String accessToken) {
        String url = KAKAO_API_URI + "/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, request, KakaoUserInfo.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching user info", e);
        }
    }

}
