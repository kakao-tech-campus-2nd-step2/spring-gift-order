package gift.Service;

import gift.DTO.ResponseKaKaoUserInfo;
import gift.DTO.ResponseKakaoTokenDTO;
import gift.Exception.KaKaoBadRequestException;
import gift.Exception.KaKaoServerErrorException;
import gift.Model.Entity.Member;
import gift.Model.Value.Email;
import gift.Model.Value.Password;
import gift.Repository.MemberRepository;
import gift.Util.JwtUtil;
import gift.Util.KakaoProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoLoginService {

    private static final String GENERATE_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String LOGIN_URI= "https://kauth.kakao.com/oauth/authorize";
    private static final String GET_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final RestClient client;
    private final KakaoProperties properties;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtill;

    public KakaoLoginService(RestClient client, KakaoProperties properties, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.client = client;
        this.properties = properties;
        this.memberRepository = memberRepository;
        this.jwtUtill=jwtUtil;
    }

    public URI requestLogin() {
        String url = generateLoginUrl();
        URI redirectionUri = client.get()
                .uri(URI.create(url))
                .retrieve()
                .toEntity(String.class)
                .getHeaders()
                .getLocation();

        return redirectionUri;
    }

    private String generateLoginUrl() {
       return UriComponentsBuilder.fromUriString(LOGIN_URI)
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUrl())
                .queryParam("response_type", "code")
                .build()
                .toUriString();
    }

    public String loginOrRegisterUser(String oauthCode) {
        String kakaoAccessToken = getToken(oauthCode);
        String userEmail;
        try {
        userEmail = client.post()
                .uri(URI.create(GET_USER_INFO_URI))
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+kakaoAccessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(ResponseKaKaoUserInfo.class)
                .getBody()
                .getKakaoAccount()
                .getEmail();
        } catch (HttpClientErrorException e) {
            throw new KaKaoBadRequestException(e.getStatusCode() + "에러 발생");
        } catch (HttpServerErrorException e) {
            throw new KaKaoServerErrorException(e.getStatusCode() + "에러발생");
        }

        Email email = new Email(userEmail);
        Member member = memberRepository.findByEmail(email).orElseGet(()->memberRepository.save(new Member(email, new Password("카카오 유저"))));
        return jwtUtill.generateToken(member);
    }

    private String getToken(String oauthCode){
        String url = GENERATE_TOKEN_URL;
        LinkedMultiValueMap<String, String> body = generateBodyForKakaoToken(oauthCode);
        try {
            String accessToken = client.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .toEntity(ResponseKakaoTokenDTO.class)
                    .getBody()
                    .getAccessToken();

            return accessToken;
        } catch (HttpClientErrorException e){
            throw new KaKaoBadRequestException(e.getStatusCode()+"에러 발생");
        } catch (HttpServerErrorException e){
            throw new KaKaoServerErrorException(e.getStatusCode()+"에러발생");
        }
    }

    private LinkedMultiValueMap<String, String> generateBodyForKakaoToken(String oauthCode) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        body.add("code", oauthCode);

        return body;
    }
}
