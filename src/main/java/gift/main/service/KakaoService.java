package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.config.KakaoProperties;
import gift.main.dto.*;
import gift.main.entity.Token;
import gift.main.entity.User;
import gift.main.handler.KaKaoUserFactory;
import gift.main.handler.TextTemplateFactory;
import gift.main.repository.TokenRepository;
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
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); //바디 객체를 만드는 부분 Map으로 만들면 되고 key 값은 카카오문서에서 요청하는 이ㄹ름으로
        map.add("grant_type", kakaoProperties.grantType());
        map.add("client_id", kakaoProperties.clientId());
        map.add("redirect_uri", kakaoProperties.redirectUri());
        map.add("code", code);

        return restClient.post() //어떤 메서드로 요청할건지 , 클라이언트 생성은 간단하게 restClient = RestClient.create();로 하면돼 나는 초기화할때하는중
                .uri(kakaoProperties.tokenRequestUri()) //요청 보낼 uri 등록하는데, 만약 요청에 쿼리 파라미터가 필요할 경우 여기다가 써야해 ?jdkfj= 이런식으로
                .contentType(CONTENT_TYPE) //보내는 바디의 타입을 지정하는 부분인데, 이건 문서보고 어떤타입을 쓸지 고민해서 넣어바바 모르겠으면 물어보고
                .body(map)
                .body("dfjk")//바디 객체를 넣는 방법
                .retrieve() //요청을 보내고 응답을 가져오는 메서드, 실질적은 요청은 여기에서 일어남
                .toEntity(KakaoToken.class) // 요청을 어떤 엔티티로 바꿀지 등록하는 부분이야 이 엔티티의 필드 명은 카카오가 전달해주는 이름이랑 동일해야하는데 자바는 보통 소문자대문자 하는데 JSon은 _으로 하잖아? 이거 매핑하는 방법이 여러개 있는데 일단 @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) 이거 전체 클래스명위에 붙여줘
                .getBody();
    }

    //카카오 엑세스 토큰을 이용한 유저정보 가져오기
    public User getKakaoProfile(KakaoToken tokenResponse) {
        KakaoProfileRequest kakaoProfileRequest = restClient.post()
                .uri(kakaoProperties.userRequestUri() + "[\"kakao_account.profile\"]")
                .contentType(CONTENT_TYPE)
                .header(AUTHORIZATION, BEARER + tokenResponse.accessToken())
                .retrieve()
                .toEntity(KakaoProfileRequest.class)
                .getBody();

        return KaKaoUserFactory.convertKakaoUserToUser(kakaoProfileRequest);
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
        //토큰을 갱신하는 부분
        renewToken(userVo);

        //요청바디 객체를 만드는 부분
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        String templateObjectJson = TextTemplateFactory.convertOrderResponseToTextTemplateJson(orderResponce);
        map.set("template_object", templateObjectJson);

        //요청을 위한 토큰을 만드는 부분
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
