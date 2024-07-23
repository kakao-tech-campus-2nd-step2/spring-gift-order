package gift.service;

import gift.controller.dto.KakaoTokenDto;
import gift.utils.ExternalApiService;
import gift.utils.config.KakaoProperties;
import gift.utils.error.KakaoLoginException;
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

        return UriComponentsBuilder.fromHttpUrl(kakaoAuthUrl)
            .queryParam("client_id", kakaoProperties.getRestApiKey())
            .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
            .queryParam("response_type", "code")
            .toUriString();
    }

    public String createKakaoToken(String code,
        String error,
        String error_description,
        String state){
        if (code==null && error!=null){
            throw new KakaoLoginException(error_description);
        }
        ResponseEntity<KakaoTokenDto> kakaoTokenDtoResponseEntity = externalApiService.postKakaoToken(
            code);
        return kakaoTokenDtoResponseEntity.getBody().getAccessToken();

    }


}
