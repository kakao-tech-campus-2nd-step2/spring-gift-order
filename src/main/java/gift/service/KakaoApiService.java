package gift.service;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.KakaoProperties;
import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import gift.exception.CustomException;

@Service
public class KakaoApiService {

    private final RestClient client;
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper;


    public KakaoApiService(KakaoProperties kakaoProperties, ObjectMapper objectMapper){
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;

        // HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        // factory.setConnectTimeout(5000);

        this.client = RestClient
                .builder()
                .build();
       
    }

    public KakaoTokenResponse getToken(String code){
        
        var url = "https://kauth.kakao.com/oauth/token";
        var body = createBody(code);

        ResponseEntity<String> response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        
        if(response.getStatusCode() != HttpStatusCode.valueOf(200)){
            throw new CustomException(response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
        }

        String jsonBody = response.getBody();

        try {
            return objectMapper.readValue(jsonBody, KakaoTokenResponse.class);
        } catch (Exception e) {
            throw new CustomException("Error parsing kakao token response", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken){

        var url = "https://kapi.kakao.com/v2/user/me";

        ResponseEntity<String> response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class);

        if(response.getStatusCode() != HttpStatusCode.valueOf(200)){
            throw new CustomException(response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
        }

        String jsonBody = response.getBody();

        try {
            KakaoUserInfoResponse kakaoUserInfoResponse = objectMapper.readValue(jsonBody, KakaoUserInfoResponse.class);
            return kakaoUserInfoResponse;
        }catch (Exception e) {
            throw new CustomException("Error parsing kakao user info response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getApiKey());
        body.add("redirect_url", kakaoProperties.getRedirectUri());
        body.add("code", code);
        return body;

    }

    // private ClientHttpRequestFactory getClientHttpRequestFactory() {
    //     HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    //     clientHttpRequestFactory.setConnectTimeout(100);
    //     clientHttpRequestFactory.setConnectionRequestTimeout(70);
    //     return clientHttpRequestFactory;
    // }
}
