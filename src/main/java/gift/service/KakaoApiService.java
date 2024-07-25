package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.KakaoProperties;
import gift.OAuthToken;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoApiService {

    private final KakaoProperties kakaoProperties;
    private RestTemplate restTemplate = new RestTemplate();

    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";

    public KakaoApiService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    private static HttpHeaders makeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> makeBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", kakaoProperties.kakaoClientId());
        body.add("redirect_uri", kakaoProperties.kakaoRedirectUrl());
        body.add("code", code);
        return body;
    }

    public String getAccessToken(String code) {
        HttpHeaders headers = makeHeaders();

        MultiValueMap<String, String> body = makeBody(code);

        // HttpHeader + HttpBody -> 하나의 Object
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);

        // Http 요청하여 response
        ResponseEntity<String> response = restTemplate.exchange(
                TOKEN_REQUEST_URI,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // 응답 데이터인 response에 있는 데이터 담는다.
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oauthToken.getAccess_token();
    }

}
