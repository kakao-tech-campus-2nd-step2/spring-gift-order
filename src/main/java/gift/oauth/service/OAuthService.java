package gift.oauth.service;

import gift.domain.member.entity.Member;
import gift.domain.member.repository.MemberRepository;
import gift.kakaoApi.service.KakaoApiService;
import gift.kakaoApi.dto.userInfo.KakaoAccount;
import gift.util.JwtUtil;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OAuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final KakaoApiService kakaoApiService;

    public OAuthService(MemberRepository memberRepository,
        JwtUtil jwtUtil,
        KakaoApiService kakaoApiService) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
    }

    public String getAccessToken(String code) {
        String accessToken = kakaoApiService.getKakaoToken(code).accessToken();
        return jwtUtil.generateToken(registerOrLoginKakoMember(accessToken));

    }

    private Member registerOrLoginKakoMember(String accessToken) {
        KakaoAccount kakaoAccount = kakaoApiService.getKakaoAccount(accessToken).kakaoAccount();
        Optional<Member> member = memberRepository.findByEmail(kakaoAccount.email());

        return member.orElseGet(
            () -> memberRepository.save(new Member(kakaoAccount.email(), "", accessToken)));

    }

}

