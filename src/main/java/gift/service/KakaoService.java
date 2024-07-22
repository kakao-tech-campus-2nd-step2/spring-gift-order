package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.user.KakaoToken;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.UnAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoService {
    @Value("${kakao.client_id}")
    String client_id;
    @Value("${kakao.redirect_uri}")
    String redirect_uri;
    String code = "";
    ObjectMapper objectMapper = new ObjectMapper();

    public String getToken() throws JsonProcessingException {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if(response.getStatusCode()==HttpStatus.UNAUTHORIZED)
            throw new UnAuthException("인증되지 않은 요청");
        if(response.getStatusCode()!=HttpStatus.OK)
            throw new BadRequestException("잘못된 요청");
        KakaoToken kakaoToken = objectMapper.readValue(response.getBody(), KakaoToken.class);
        return kakaoToken.access_token();
    }
}
