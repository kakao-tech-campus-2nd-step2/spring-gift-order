package gift.oauth.business.service;

import gift.global.util.EncryptionUtils;
import gift.member.business.dto.JwtToken;
import gift.member.business.dto.MemberLoginDto;
import gift.member.business.dto.MemberRegisterDto;
import gift.member.business.service.MemberService;
import gift.member.persistence.repository.MemberRepository;
import gift.oauth.business.client.OAuthApiClient;
import gift.oauth.business.dto.OAuthParam;
import gift.global.domain.OAuthProvider;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {
    private final Map<OAuthProvider, OAuthApiClient> clients;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public OAuthService(List<OAuthApiClient> clients, MemberRepository memberRepository,
        MemberService memberService) {
        this.clients = clients.stream().collect(
            Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity()));
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Transactional
    public JwtToken loginOrRegister(OAuthParam param) {
        OAuthApiClient client = clients.get(param.oAuthProvider());
        var accessToken = client.getAccessToken(param);
        var email = client.getEmail(accessToken, param);
        String password = null;
        try {
            password = EncryptionUtils.encrypt(email, param.secretKey());
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 암호화 실패");
        }
        if(memberRepository.existsByEmail(email)) {
            var memberLoginDto = new MemberLoginDto(email, password);
            return memberService.loginMember(memberLoginDto);
        }
        var memberRegisterDto = new MemberRegisterDto(email, password);
        return memberService.registerMember(memberRegisterDto);
    }
}
