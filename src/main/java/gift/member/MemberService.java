package gift.member;

import static gift.exception.ErrorMessage.MEMBER_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static gift.exception.ErrorMessage.WRONG_PASSWORD;

import gift.exception.FailedLoginException;
import gift.token.JwtProvider;
import gift.token.KakaoJwkProvider;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    public final MemberRepository memberRepository;
    public final JwtProvider jwtProvider;
    private final KakaoJwkProvider kakaoJwkProvider;

    public MemberService(
        MemberRepository memberRepository,
        JwtProvider jwtProvider,
        KakaoJwkProvider kakaoJwkProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.kakaoJwkProvider = kakaoJwkProvider;
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

    public void registerIfNotExistsByIdToken(String idToken) {
        Pair<String, String> emailAndSub = kakaoJwkProvider.getEmailAndSub(idToken);

        if (!memberRepository.existsById(emailAndSub.getFirst())) {
            memberRepository.save(new Member(emailAndSub.getFirst(), emailAndSub.getSecond()));
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
