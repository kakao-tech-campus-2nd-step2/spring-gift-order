package gift.service.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.JwtProvider;
import gift.domain.member.Member;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import gift.repository.MemberRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoLoginService {

    private final KakaoProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public KakaoLoginService(KakaoProperties properties, RestTemplate restTemplate, ObjectMapper objectMapper, MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.properties = properties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public HttpHeaders getRedirectHeaders() {
        String url = createLoginUrl();
        HttpHeaders headers = createRedirectHeaders(url);

        return headers;
    }

    public TokenResponse getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = createFormBody(code);
        String accessToken = restTemplate.postForObject(url, body, KakaoTokenInfoResponse.class).getAccessToken();
        Member kakaoMember = getKakaoMember(accessToken);
        String jwt = jwtProvider.create(kakaoMember);

        TokenResponse tokenResponse = new TokenResponse(accessToken, jwt);

        return tokenResponse;
    }

    private Member getKakaoMember(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GiftException(ErrorCode.KAKAO_USER_RETRIEVAL_FAILED);
        }

        JsonNode jsonNode = parseJson(response.getBody());
        Long kakaoId = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Member m = new Member.MemberBuilder()
                            .kakaoId(kakaoId)
                            .name(nickname)
                            .build();

                    return memberRepository.save(m);
                });

        return member;
    }

    private JsonNode parseJson(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new GiftException(ErrorCode.JSON_PARSING_FAILED);
        }
    }

    private HttpHeaders createRedirectHeaders(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return headers;
    }

    private String createLoginUrl() {
        String url = "https://kauth.kakao.com/oauth/authorize?&response_type=code"
                + "&redirect_uri=" + properties.redirectUrl()
                + "&client_id=" + properties.clientId();
        return url;
    }

    private LinkedMultiValueMap<String, String> createFormBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }

}
