package gift.service;

import gift.auth.KakaoProperties;
import gift.domain.KakaoProfile;
import gift.domain.KakaoToken;
import gift.domain.Token;
import gift.entity.MemberEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoService {

    private final static String KAKAO_GET_AUTH_CODE_URL = "https://kauth.kakao.com/oauth/authorize";
    private final static String KAKAO_GET_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_GET_USER_PROFILE = "https://kapi.kakao.com/v2/user/me";
    private final KakaoProperties kakaoProperties;
    private final MemberService memberService;
    private final RestClient restClient;

    public KakaoService(KakaoProperties kakaoProperties, MemberService memberService) {
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
        restClient = RestClient.create();
    }

    public String getCode() {
        String codeUri = UriComponentsBuilder.fromHttpUrl(KAKAO_GET_AUTH_CODE_URL)
            .queryParam("client_id", kakaoProperties.restApiKey())
            .queryParam("redirect_uri", kakaoProperties.redirectUrl())
            .queryParam("response_type", "code")
            .toUriString();
        return codeUri;
    }

    public KakaoToken getToken(String code){
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        return restClient.post()
            .uri(KAKAO_GET_TOKEN_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoToken.class)
            .getBody();
    }

    public KakaoProfile getUserProfile(String accessToken) {
        return restClient.get()
            .uri(KAKAO_GET_USER_PROFILE)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(KakaoProfile.class)
            .getBody();
    }

    public Token login(String code) {
        KakaoToken kakaoToken = getToken(code);
        KakaoProfile profile = getUserProfile(kakaoToken.access_token());
        return memberService.kakaoLogin(profile.id());
    }

}
