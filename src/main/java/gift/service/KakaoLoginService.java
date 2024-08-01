package gift.service;

import gift.domain.member.kakao.KakaoTokenResponse;
import gift.domain.member.kakao.KakaoUserInfoResponse;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final RestClient restClient;
    private final MemberService memberService;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoLoginService(RestClient restClient, MemberService memberService) {
        this.restClient = restClient;
        this.memberService = memberService;
    }

    public String getLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&redirect_uri="
            + redirectUri + "&client_id=" + clientId;
    }

    public String getToken(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", redirectUri);
        body.add("code", code);

        return Objects.requireNonNull(restClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .body(body)
                .retrieve()
                .body(KakaoTokenResponse.class))
            .accessToken();
    }

    public Long getKakaoId(String accessToken) {
        return Objects.requireNonNull(restClient
                .post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .body(KakaoUserInfoResponse.class))
            .id();
    }
}
