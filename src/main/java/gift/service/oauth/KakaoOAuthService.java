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
import gift.model.Token;
import gift.repository.TokenRepository;
import gift.service.MemberService;
import gift.util.JWTUtil;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoOAuthService {

    private final KakaoApiClient kakaoApiClient;
    private final MemberService memberService;
    private final TokenRepository tokenRepository;
    private final JWTUtil jwtUtil;

    @Value("${kakao.password}")
    private String kakaoPassword;

    public KakaoOAuthService(
        KakaoApiClient kakaoApiClient,
        MemberService memberService,
        TokenRepository tokenRepository,
        JWTUtil jwtUtil
    ) {
        this.kakaoApiClient = kakaoApiClient;
        this.memberService = memberService;
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        return kakaoApiClient.getAccessToken(code);
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        return kakaoApiClient.getUserInfo(accessToken);
    }

    public KakaoUnlinkResponse unlinkUser(Long memberId) {
        Token token = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));
        return kakaoApiClient.unlinkUser(token.getAccessToken());
    }

    public KakaoScopeResponse getUserScopes(Long memberId) {
        Token token = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));
        return kakaoApiClient.getUserScopes(token.getAccessToken());
    }

    public KakaoUserResponse getUserInfo(Long memberId) {
        Token token = tokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("토큰을 찾을 수 없습니다."));
        return kakaoApiClient.getUserInfo(token.getAccessToken());
    }

    @Transactional
    public MemberResponse registerOrLoginKakaoUser(KakaoUserResponse userResponse) {
        try {
            MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                userResponse.email(),
                kakaoPassword,
                RegisterType.KAKAO
            );
            System.out.println("회원가입 완료");
            return memberService.registerMember(registerRequest);
        } catch (EmailAlreadyUsedException e) {
            System.out.println("로그인 완료");
            return memberService.loginKakaoMember(userResponse.email());
        }
    }

    @Transactional
    public void saveToken(KakaoTokenResponse tokenResponse, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        Token token = new Token(
            memberId,
            tokenResponse.accessToken(),
            tokenResponse.refreshToken(),
            now.plusSeconds(tokenResponse.expiresIn()),
            now.plusSeconds(tokenResponse.refreshTokenExpiresIn())
        );
        tokenRepository.save(token);
    }

    public String generateJwt(Long memberId, String email) {
        return jwtUtil.generateToken(memberId, email);
    }
}
