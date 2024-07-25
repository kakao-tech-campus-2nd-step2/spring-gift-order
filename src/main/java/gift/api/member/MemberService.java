package gift.api.member;

import gift.api.member.config.KakaoProperties;
import gift.api.member.dto.MemberRequest;
import gift.global.exception.ForbiddenMemberException;
import gift.global.exception.UnauthorizedMemberException;
import gift.global.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoProperties properties;

    public MemberService(MemberRepository memberRepository, KakaoProperties properties) {
        this.memberRepository = memberRepository;
        this.properties = properties;
    }

    @Transactional
    public Long register(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.email())) {
            throw new EmailAlreadyExistsException();
        }
        return memberRepository.save(memberRequest.toEntity()).getId();
    }

    public void login(MemberRequest memberRequest, String token) {
        if (memberRepository.existsByEmailAndPassword(memberRequest.email(), memberRequest.password())) {
            Long id = memberRepository.findByEmail(memberRequest.email()).get().getId();
            if (token.equals(JwtUtil.generateAccessToken(id, memberRequest.email(), memberRequest.role()))) {
                return;
            }
            throw new UnauthorizedMemberException();
        }
        throw new ForbiddenMemberException();
    }

    public LinkedMultiValueMap<Object, Object> createBody(String code) {
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.url().redirect());
        body.add("code", code);
        return body;
    }
}
