package gift.service;

import gift.dto.response.KakaoTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String CLIENT_ID = "19b572e7babb2760f39ab8bfbc11a095";


    private RestClient client = RestClient.create();

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
        client.get()
                .uri("https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={%s}", CLIENT_ID);
    }
}
