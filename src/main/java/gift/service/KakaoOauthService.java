package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.customException.JsonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoOauthService {

    @Value("${kakao_api_scope}")
    private String SCOPE;

    @Value("${kakao_api_response_type}")
    private String RESPONSE_TYPE;

    @Value("${kakao_api_redirect_uri}")
    private String REDIRECT_URI;

    @Value("${kakao_api_client_id}")
    private String CLIENT_ID;

    @Value("${kakao_api_grant_type}")
    private String GRANT_TYPE;
    private final String ACCESS_TOKEN_API_URL = "https://kauth.kakao.com/oauth/token";
    private final String USER_INFORMATION_API_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoOauthService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public StringBuilder makeKakaOauthParameter(){
        StringBuilder builder = new StringBuilder();
        builder.append("scope=").append(SCOPE).append("&")
                .append("response_type=").append(RESPONSE_TYPE).append("&")
                .append("redirect_uri=").append(REDIRECT_URI).append("&")
                .append("client_id=").append(CLIENT_ID);

        return builder;
    }

    public String getKakaoAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        RequestEntity request = new RequestEntity<>(makeBody(code), headers, HttpMethod.POST, URI.create(ACCESS_TOKEN_API_URL));
        ResponseEntity<String> response = restTemplate.exchange(ACCESS_TOKEN_API_URL, HttpMethod.POST, request, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        }catch (JsonProcessingException e) {
            throw new JsonException();
        }catch (Exception e) {
            throw new RuntimeException("예상하지 못한 EXCEPTION 발생");
        }
    }

    public String getKakaoUserInformation(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        RequestEntity request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(USER_INFORMATION_API_URL));
        ResponseEntity<String> response = restTemplate.exchange(USER_INFORMATION_API_URL, HttpMethod.POST, request, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("id").asText();
        } catch (JsonProcessingException e) {
            throw new JsonException();
        }catch (Exception e) {
            throw new RuntimeException("예상하지 못한 EXCEPTION 발생");
        }
    }

    private LinkedMultiValueMap<String, String> makeBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        return body;
    }


}
