package gift.service;

import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoLoginService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoTokenResponse getKakaoToken(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                    request,
                    KakaoTokenResponse.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("access token 얻기 실패: HTTP 상태 " + response.getStatusCode());
            }

            KakaoTokenResponse bodyResponse = response.getBody();
            if (bodyResponse == null) {
                throw new RuntimeException("access token 얻기 실패: 응답 바디가 null입니다.");
            }

            return bodyResponse;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("access token 얻기 실패: "  + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("access token을 얻는 중 예상치 못한 오류가 발생했습니다", e);
        }
    }

    public KakaoProfileResponse getUserProfile(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<KakaoProfileResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    KakaoProfileResponse.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("사용자 정보 얻기 실패: HTTP 상태 " + response.getStatusCode());
            }

            KakaoProfileResponse body = response.getBody();
            if (body == null) {
                throw new RuntimeException("사용자 정보 얻기 실패: 응답 바디가 null입니다.");
            }

            return body;

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("사용자 정보 얻기 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보를 얻는 중 예상치 못한 오류가 발생했습니다", e);
        }
    }
}
