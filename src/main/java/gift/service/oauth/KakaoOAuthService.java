package gift.service.oauth;

import gift.client.KakaoApiClient;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.exception.member.EmailAlreadyUsedException;
import gift.model.RegisterType;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final MemberService memberService;

    @Value("${kakao.password}")
    private String kakaoPassword;

    public KakaoOAuthService(KakaoApiClient kakaoApiClient, MemberService memberService) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberService = memberService;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        return kakaoApiClient.getAccessToken(code);
    }

    public KakaoUnlinkResponse unlinkUser(String accessToken) {
        return kakaoApiClient.unlinkUser(accessToken);
    }

    public KakaoScopeResponse getUserScopes(String accessToken) {
        return kakaoApiClient.getUserScopes(accessToken);
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        return kakaoApiClient.getUserInfo(accessToken);
    }

    public MemberResponse registerOrLoginKakaoUser(KakaoUserResponse userResponse) {
        try {
            MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                userResponse.email(),
                kakaoPassword,
                RegisterType.KAKAO
            );
            return memberService.registerMember(registerRequest);
        } catch (EmailAlreadyUsedException e) {
            return memberService.loginKakaoMember(userResponse.email());
        }
    }
}
