package gift.product.service;

import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties properties;
    private final MemberRepository memberRepository;

    public KakaoService(KakaoProperties properties, MemberRepository memberRepository) {
        this.properties = properties;
        this.memberRepository = memberRepository;
    }

    public String getAuthCode() {
        return "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
            + "&redirect_uri=" + properties.redirectUrl()
            + "&client_id=" + properties.clientId();
    }

    public void getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody(code);
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(Map.class);
        if (response == null)
            throw new AssertionError();
        String accessToken = response.get("access_token").toString();
        signUpAndLogin(accessToken);
    }

    public void signUpAndLogin(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        var response = client.get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(Map.class);
        if (response == null)
            throw new AssertionError();
        String memberId = response.get("id").toString();
        if(!memberRepository.existsByEmail(memberId))
            memberRepository.save(new Member(memberId, "kakao"));
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}