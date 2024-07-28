package gift.service.kakaoAuth;

import gift.service.member.MemberService;
import gift.web.dto.MemberDto;
import gift.web.dto.Token;
import gift.web.exception.MemberNotFoundException;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final MemberService memberService;

    @Value("${kakao.token-post-url}")
    private String kakaoTokenPostUrl;

    @Value("${kakao.member-info-post-url}")
    private String kakaoMemberInfoPostUrl;

    @Value("${kakao.auth-setting-url}")
    private String kakaoAuthSettingUrl;

    public KakaoAuthService(RestClient restClient, KakaoProperties kakaoProperties,
        MemberService memberService) {
        this.restClient = restClient;
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
    }

    public boolean isSignedUp(KakaoInfo kakaoInfo) {
        try {
            MemberDto memberDto = memberService.getMemberByEmail(kakaoInfo.email());
        } catch (MemberNotFoundException e) {
            return false;
        }
        return true;
    }

    public MemberDto getMemberInfo(KakaoInfo kakaoInfo) {
        return memberService.getMemberByEmail(kakaoInfo.email());
    }

    public String getKakaoAuthUrl() {
        StringBuffer str = new StringBuffer();
        str.append(kakaoAuthSettingUrl);
        str.append("&redirect_uri=" + kakaoProperties.redirectUri());
        str.append("&client_id=" + kakaoProperties.clientId());

        return str.toString();
    }

    public Token receiveToken(String code) {
        var body = kakaoProperties.createBody(code);

        var response = restClient.post()
            .uri(URI.create(kakaoTokenPostUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(Map.class);

        return new Token(response.getBody().get("access_token").toString());
    }

    public KakaoInfo getMemberInfoFromKakaoServer(Token accessToken) {

        ResponseEntity<Map<String, Object>> response = restClient.post()
            .uri(URI.create(kakaoMemberInfoPostUrl))
            .header("Authorization", "Bearer " + accessToken.token())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, String> responseBody = (Map<String, String>) response.getBody().get("kakao_account");

        return new KakaoInfo(responseBody.get("email"));
    }
}
