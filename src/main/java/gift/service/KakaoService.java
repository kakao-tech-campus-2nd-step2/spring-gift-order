package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoService {

    @Value( "${kakao.client_id}" )
    String client_id;

    @Value( "${kakao.redirect_uri}" )
    String redirect_uri;

    String code = "";

    public String getClientId() {
        return client_id;
    }

    public String getRedirectUri() {
        return redirect_uri;
    }

    public String getToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";

        // HTTP 헤더 설정
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // HTTP 요청 바디 설정
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        // 요청 엔티티 생성
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        // RestTemplate을 사용하여 POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // JSON 응답을 파싱하여 액세스 토큰 추출
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            return (String) responseBody.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            Long id = (Long) responseBody.get("id");
            return id.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
