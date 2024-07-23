package gift.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.dto.AuthResponse;
import gift.auth.util.KakaoAuthUtil;
import gift.member.application.MemberService;
import gift.member.dto.MemberDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService {

    private final RestTemplate restTemplate;
    private final KakaoAuthUtil kakaoAuthUtil;
    private final MemberService memberService;

    public KakaoAuthService(RestTemplate restTemplate,
                            KakaoAuthUtil kakaoAuthUtil,
                            MemberService memberService) {
        this.restTemplate = restTemplate;
        this.kakaoAuthUtil = kakaoAuthUtil;
        this.memberService = memberService;
    }

    public String getKakaoAuthUrl() {
        return kakaoAuthUtil.getKakaoAuthUrl();
    }

    public String getKakaoUserEmail(String userInfo) {
        return kakaoAuthUtil.generateKakaoEmail(userInfo);
    }

    public String getAccessToken(String authCode) throws Exception {
        String url = "https://kauth.kakao.com/oauth/token";
        String responseJson = restTemplate.postForObject(
                url,
                kakaoAuthUtil.getRequestWithPost(url, authCode),
                String.class
        );

        return kakaoAuthUtil.extractValueFromJson(responseJson, "access_token");
    }

    public String getUserId(String token) throws JsonProcessingException {
        String url = "https://kapi.kakao.com/v2/user/me";
        String responseJson = restTemplate.exchange(
                url,
                HttpMethod.GET,
                kakaoAuthUtil.getRequestWithGet(url, token),
                String.class
        ).getBody();

        return kakaoAuthUtil.extractValueFromJson(responseJson, "id");
    }

    public ResponseEntity<Object> getResponseOfKakaoLogin(String code) {
        try {
            String accessToken = getAccessToken(code);
            String kakaoUserId = getUserId(accessToken);
            String email = getKakaoUserEmail(kakaoUserId);

            MemberDto member = memberService.getMemberByEmail(email)
                    .orElseGet(() -> {
                        MemberDto newMember = new MemberDto(
                                email,
                                kakaoAuthUtil.generateTemporaryPassword()
                        );
                        memberService.registerMember(newMember);
                        return newMember;
                    });

            String token = memberService.authenticate(member);
            return ResponseEntity.ok(AuthResponse.of(token));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError()
                    .body("Error : " + exception.getMessage());
        }
    }

}
