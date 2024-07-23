package gift.auth.application;

import gift.auth.dto.AuthResponse;
import gift.auth.dto.KakaoTokenResponse;
import gift.auth.dto.KakaoUserInfoResponse;
import gift.auth.util.KakaoAuthUtil;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
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

    public String getKakaoUserEmail(Long userId) {
        return kakaoAuthUtil.generateKakaoEmail(userId);
    }

    public String getAccessToken(String authCode) {
        String url = "https://kauth.kakao.com/oauth/token";
        KakaoTokenResponse response = restTemplate.postForObject(
                url,
                kakaoAuthUtil.getRequestWithPost(url, authCode),
                KakaoTokenResponse.class
        );

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response.accessToken();
    }

    public Long getUserId(String token) {
        String url = "https://kapi.kakao.com/v2/user/me";
        KakaoUserInfoResponse response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                kakaoAuthUtil.getRequestWithGet(url, token),
                KakaoUserInfoResponse.class
        ).getBody();

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response.id();
    }

    public ResponseEntity<Object> getResponseOfKakaoLogin(String code) {
        try {
            String accessToken = getAccessToken(code);
            Long kakaoUserId = getUserId(accessToken);
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
