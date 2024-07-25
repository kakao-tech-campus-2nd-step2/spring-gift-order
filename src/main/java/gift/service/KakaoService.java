package gift.service;

import gift.config.KakaoProperties;
import gift.domain.member.MemberRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class KakaoService {
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private final MemberRepository memberRepository;
    private final KakaoProperties kakaoProperties;

    public KakaoService(MemberRepository memberRepository, KakaoProperties kakaoProperties) {
        this.memberRepository = memberRepository;
        this.kakaoProperties = kakaoProperties;
    }

    //TODO
    //토큰 파싱하고 유효성 검사하고 사용자 정보 조회
    //1. 사용자 정보 확인
    //2. (신규회원일경우) 가입 처리
    //3. 로그인 완료

    public void getKakaoToken(String code) {
        //code 넣고 토큰 받아오기
        RestClient client = RestClient.builder().build();
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectURL());
        body.add("code", code);

        var response = client.post()
                .uri(URI.create(KAKAO_TOKEN_URI))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            //TODO 성공로직
//        }
        //TODO 에러처리 로직
        //<200 OK OK,{"access_token":"eJ7Tu-TAR8JW4SXoOH6DLyp1UZfhVqC6AAAAAQo8JB8AAAGQ6ucoXLZbzBbpXusm","token_type":"bearer","refresh_token":"xA9fCgg9ClNRHsmjK11PFYVHSd0NpwDeAAAAAgo8JB8AAAGQ6ucoWbZbzBbpXusm","expires_in":21599,"scope":"talk_message","refresh_token_expires_in":5183999},
    }
}


