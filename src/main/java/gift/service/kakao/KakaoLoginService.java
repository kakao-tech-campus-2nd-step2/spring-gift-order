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
import java.util.Optional;

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
        return createRedirectHeaders(url);
    }

    public TokenResponse processKakaoAuth(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> body = createFormBody(code);

        KakaoTokenInfoResponse tokenInfo = getTokenInfoFromKakao(url, body);

        String accessToken = tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();

        Member kakaoMember = getKakaoMember(accessToken, refreshToken);
        String jwt = jwtProvider.create(kakaoMember);

        return new TokenResponse(accessToken, jwt);
    }

    private KakaoTokenInfoResponse getTokenInfoFromKakao(String url, LinkedMultiValueMap<String, String> body) {
        return Optional.ofNullable(restTemplate.postForObject(url, body, KakaoTokenInfoResponse.class))
                .orElseThrow(() -> new GiftException(ErrorCode.KAKAO_TOKEN_ISSUANCE_FAILED));
    }

    private Member getKakaoMember(String accessToken, String refreshToken) {
        ResponseEntity<String> response = getKakaoUserResponse(accessToken);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GiftException(ErrorCode.KAKAO_USER_RETRIEVAL_FAILED);
        }

        JsonNode jsonNode = parseJson(response.getBody());
        Long kakaoId = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> createAndSaveMember(kakaoId, nickname, accessToken, refreshToken));
    }

    private ResponseEntity<String> getKakaoUserResponse(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, String.class);
    }

    private Member createAndSaveMember(Long kakaoId, String nickname, String accessToken, String refreshToken) {
        Member newMember = new Member.MemberBuilder()
                .kakaoId(kakaoId)
                .name(nickname)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return memberRepository.save(newMember);
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
        return "https://kauth.kakao.com/oauth/authorize?&response_type=code"
                + "&redirect_uri=" + properties.redirectUrl()
                + "&client_id=" + properties.clientId();
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
