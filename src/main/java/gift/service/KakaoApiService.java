package gift.service;

import gift.controller.dto.KakaoApiDTO;
import gift.utils.ExternalApiService;
import gift.utils.config.KakaoProperties;
import gift.utils.error.KakaoLoginException;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {
    private final ExternalApiService externalApiService;
    private final KakaoProperties kakaoProperties;

    public KakaoApiService(ExternalApiService externalApiService, KakaoProperties kakaoProperties) {
        this.externalApiService = externalApiService;
        this.kakaoProperties = kakaoProperties;
    }


    public String createKakaoCode(){
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize";

        String url = UriComponentsBuilder.fromHttpUrl(kakaoAuthUrl)
            .queryParam("client_id", kakaoProperties.getRestApiKey())
            .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
            .queryParam("response_type", "code")
            .toUriString();
        return url;
    }

    public String createKakaoToken(String code,
        String error,
        String error_description,
        String state){
        if (code==null && error!=null){
            throw new KakaoLoginException("Kakao Login Error");
        }
        ResponseEntity<Map<String, Object>> mapResponseEntity = externalApiService.postKakaoToken(
            code);
        String accessToken = (String) mapResponseEntity.getBody().get("access_token");
        return accessToken;

    }


}
