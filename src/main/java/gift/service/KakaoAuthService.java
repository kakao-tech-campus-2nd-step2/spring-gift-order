package gift.service;

import gift.config.KakaoProperties;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public KakaoAuthService(RestTemplateBuilder builder,  KakaoProperties kakaoProperties, MemberRepository memberRepository, JwtUtil jwtUtil) {
        restTemplate = builder.build();
        this.kakaoProperties = kakaoProperties;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = restTemplate.exchange(request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return responseBody.get("access_token").toString();
    }

    public String getKakaoUserId(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", "Bearer " + accessToken);
        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        var response = restTemplate.exchange(request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        return responseBody.get("id").toString();
    }

    public String registerKakaoMember(String kakaoUserId, String token) {
        String email = kakaoUserId + "@kakao.com";
        String password = "password";
        Member member = new Member(email, password, token);
        memberRepository.save(member);
        return JwtUtil.generateToken(email);
    }
}