package gift.member;

import static gift.exception.ErrorMessage.MEMBER_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static gift.exception.ErrorMessage.WRONG_PASSWORD;

import gift.exception.FailedLoginException;
import gift.token.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    public final MemberRepository memberRepository;
    public final JwtProvider jwtProvider;

    public MemberService(
        MemberRepository memberRepository,
        JwtProvider jwtProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public Member getMember(String email) {
        return memberRepository.findById(email)
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND));
    }

    public String register(MemberDTO memberDTO) {
        memberRepository.findById(memberDTO.getEmail())
            .ifPresentOrElse(
                e -> {
                    throw new IllegalArgumentException(MEMBER_ALREADY_EXISTS);
                },
                () -> memberRepository.save(memberDTO.toEntity())
            );

        return jwtProvider.generateToken(memberDTO.toTokenDTO());
    }

    public void registerIfNotExists(String email, String password) {
        if (!memberRepository.existsById(email)) {
            memberRepository.save(new Member(email, password));
        }
    }

    @Transactional(readOnly = true)
    public String login(MemberDTO memberDTO) {
        Member findMember = memberRepository.findById(memberDTO.getEmail())
            .orElseThrow(() -> new FailedLoginException(MEMBER_NOT_FOUND));

        verifyPassword(findMember, memberDTO);

        return jwtProvider.generateToken(memberDTO.toTokenDTO());
    }

    private void verifyPassword(Member member, MemberDTO memberDTO) {
        if (!member.isSamePassword(memberDTO.toEntity())) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
    }
}
