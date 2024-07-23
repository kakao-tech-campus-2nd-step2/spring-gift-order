package gift.service;

import gift.auth.KakaoProperties;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class KakaoService {

    private final static String KAKAO_GET_AUTH_CODE_URL = "https://kauth.kakao.com/oauth/authorize";
    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
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

}
