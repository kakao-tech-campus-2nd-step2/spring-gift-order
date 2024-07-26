package gift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoLoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();
    @Value("${my.client_id}")
    private String client_id;

    @Value("${my.code}")
    private String code;
    @Test
    void test1() throws JsonProcessingException {
        // 요청 URL
        var url = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // 요청 바디 설정
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", code); // authorizationCode 값을 여기 넣으세요
        System.out.println(client_id);

        // RequestEntity 객체 생성
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 보내기
        ResponseEntity<KakaoLoginResponse> response = restTemplate.exchange(request, KakaoLoginResponse.class);

        // 응답 결과 출력
        System.out.println("Response: " + response.getBody());
        System.out.println(response.getBody().access_token());
        message(response.getBody().access_token());


    }

    void message(String token) throws JsonProcessingException {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization","Bearer " + token);

        Map<String, Object> link = new HashMap<>();
        link.put("web_url", "https://developers.kakao.com");
        link.put("mobile_web_url", "https://developers.kakao.com");

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", "텍스트 영역입니다. 최대 200자 표시 가능합니다.");
        templateObject.put("link", link);
        templateObject.put("button_title", "바로 확인");

        // 데이터 URL 인코딩
        String templateObjectJson = new ObjectMapper().writeValueAsString(templateObject);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate을 통해 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 응답 출력
        System.out.println("Response: " + response.getBody());
    }
}
