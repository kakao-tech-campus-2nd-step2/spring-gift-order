package gift.service;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String AUTHORIZATION_CODE_REQUEST_URI = "https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code&redirect_uri=http://localhost:8080&client_id={%s}";
    @Value("${clientId}")
    private String CLIENT_ID;
    private RestClient client = RestClient.create();

    MemberService memberService;
    public KakaoLoginService(MemberService memberService) {
        this.memberService = memberService;
    }

    public KakaoTokenResponse getToken(String code) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("grant_type", GRANT_TYPE);
        multiValueMap.add("redirect_uri", REDIRECT_URI);
        multiValueMap.add("client_id", CLIENT_ID);
        multiValueMap.add("code", code);


        return client.post()
                .uri(TOKEN_REQUEST_URI)
                .body(multiValueMap)
                .retrieve()
                .body(KakaoTokenResponse.class);
    }

    public void requestAuthorizationCode() {
        System.out.println("here");
        String retrieve = client.get()
                .uri(AUTHORIZATION_CODE_REQUEST_URI, CLIENT_ID)
                .retrieve()
                .body(String.class);
        System.out.println(retrieve);
    }
    public String getEmail(String token) {

        KakaoUserInfoResponse body = client.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", String.format("Bearer %s", token))
                .retrieve()
                .body(KakaoUserInfoResponse.class);
        return body.kakao_account().email();
    }
}
