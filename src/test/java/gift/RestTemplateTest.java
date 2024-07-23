package gift;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoLoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1(){
        // 요청 URL
        var url = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // 요청 바디 설정
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "c19991678d82e3768f54412c03c96c96");
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", "7oS03_Nunlhcj08vwpVwvjRGf6LFJm8VwSZGdj_5RzXOKJZKaZS3RgAAAAQKPXObAAABkOCNQbrGDcCf5rkkeA"); // authorizationCode 값을 여기 넣으세요

        // RequestEntity 객체 생성
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 보내기
        ResponseEntity<KakaoLoginResponse> response = restTemplate.exchange(request, KakaoLoginResponse.class);

        // 응답 결과 출력
        System.out.println("Response: " + response.getBody());
        System.out.println(response.getBody().access_token());

//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(response.getBody());
//
//        // 특정 값 추출 (예: access_token)
//        String accessToken = rootNode.path("access_token").asText();
//        System.out.println("Access Token: " + accessToken);

    }
}
