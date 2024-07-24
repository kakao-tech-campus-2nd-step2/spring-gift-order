package gift.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.config.KakaoProperties;
import gift.main.dto.*;
import gift.main.entity.Token;
import gift.main.entity.User;
import gift.main.repository.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;


import static io.jsonwebtoken.lang.Strings.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class KakaoService {

    private static final MediaType CONTENT_TYPE = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final TokenRepository tokenRepository;

    public KakaoService(KakaoProperties kakaoProperties,
                        TokenRepository tokenRepository) {
        this.kakaoProperties = kakaoProperties;
        this.tokenRepository = tokenRepository;
        restClient = RestClient.create();
    }

    //카카오 인가코드를 이용한 엑세스 토큰 요청하기
    public KakaoToken requestKakaoToken(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", kakaoProperties.grantType());
        map.add("client_id", kakaoProperties.clientId());
        map.add("redirect_uri", kakaoProperties.redirectUri());
        map.add("code", code);

        return restClient.post()
                .uri(kakaoProperties.tokenRequestUri())
                .contentType(CONTENT_TYPE)
                .body(map)
                .retrieve()
                .toEntity(KakaoToken.class)
                .getBody();
    }

    //카카오 엑세스 토큰을 이용한 유저정보 가져오기
    public KakaoUser getKakaoProfile(KakaoToken tokenResponse) {
        KakaoProfile kakaoProfile = restClient.post()
                .uri(kakaoProperties.userRequestUri() + "[\"kakao_account.profile\"]")
                .contentType(CONTENT_TYPE)
                .header(AUTHORIZATION, BEARER + tokenResponse.accessToken())
                .retrieve()
                .toEntity(KakaoProfile.class)
                .getBody();

        return new KakaoUser(kakaoProfile, kakaoProperties.password());

    }

    public void SaveToken(User user, KakaoToken kakaoToken) {
        Token token = new Token(user, kakaoToken);
        tokenRepository.save(token);
    }

    @Transactional
    public void renewToken(UserVo userVo) {

        Token token = tokenRepository.findById(userVo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOKEN));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", kakaoProperties.clientId());
        map.add("refresh_token",token.getRefreshToken());

        KakaoToken renewToken= restClient.post()
                .uri(kakaoProperties.tokenRenewalRequestUri())
                .contentType(CONTENT_TYPE)
                .body(map)
                .retrieve()
                .toEntity(KakaoToken.class)
                .getBody();

        token.updete(renewToken);
    }

    public void SendOrderMessage(OrderResponce orderResponce, UserVo userVo)  {
        renewToken(userVo);
        ObjectMapper objectMapper = new ObjectMapper();

        TemplateObject templateObject = new TemplateObject("text", orderResponce.toString(), new Link("link"));

        String templateObjectJson = null;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        // HTTP 요청 데이터 설정
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        map.set("template_object", templateObjectJson);

        Token token = tokenRepository.findByUserId(userVo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOKEN));

        restClient.post()
                .uri(kakaoProperties.messageRequestUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(AUTHORIZATION, BEARER + token.getAccessToken())
                .body(map)
                .retrieve()
                .toEntity(String.class);

    }


}
