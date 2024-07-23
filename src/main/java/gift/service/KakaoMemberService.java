package gift.service;

import gift.domain.KakaoMember;
import gift.dto.response.KakaoMemberResponse;
import gift.exception.KakaoMemberNotFoundException;
import gift.repository.KakaoMemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

import static gift.constants.Messages.NOT_FOUND_KAKAO_MEMBER;

@Service
public class KakaoMemberService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final KakaoMemberRepository kakaoMemberRepository;

    public KakaoMemberService(KakaoMemberRepository kakaoMemberRepository) {
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    public String getKakaoCodeUrl() {
        String baseUrl = "https://kauth.kakao.com/oauth/authorize";
        return baseUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
    }

    public String processToken(String header) {
        header = header.substring(7);
        return header;
    }

    @Transactional(readOnly = true)
    public boolean checkTokenExists(String token) {
        return kakaoMemberRepository.existsByAccessToken(token);
    }

    public String getKakaoToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        var response = new RestTemplate().exchange(request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");
        System.out.println("Access Token: " + accessToken);
        return accessToken;
    }

    @Transactional
    public void save(String token, String email) {
        kakaoMemberRepository.save(new KakaoMember(token, email));
    }

    @Transactional(readOnly = true)
    public KakaoMemberResponse findByAccessToken(String token){
        return kakaoMemberRepository.findByAccessToken(token)
                .map(KakaoMemberResponse::from)
                .orElseThrow(()->new KakaoMemberNotFoundException(NOT_FOUND_KAKAO_MEMBER));
    }
}