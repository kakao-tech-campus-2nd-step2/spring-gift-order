package gift.domain.service.member;

import gift.domain.dto.request.member.KakaoOauthMemberRequest;
import gift.domain.dto.request.member.MemberRequest;
import gift.domain.entity.KakaoOauthMember;
import gift.domain.entity.Member;
import gift.domain.exception.notFound.MemberNotFoundException;
import gift.domain.repository.KakaoOauthMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoOauthMemberService implements DerivedMemberService<KakaoOauthMember, KakaoOauthMemberRequest> {

    private final KakaoOauthMemberRepository kakaoOauthMemberRepository;

    public KakaoOauthMemberService(KakaoOauthMemberRepository kakaoOauthMemberRepository) {
        this.kakaoOauthMemberRepository = kakaoOauthMemberRepository;
    }

    @Override
    @Transactional
    public Member registerMember(MemberRequest requestDto, Member member) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Member loginMember(MemberRequest requestDto, Member member) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public KakaoOauthMember findDerivedMemberBy(Member member) {
        return kakaoOauthMemberRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public KakaoOauthMemberRequest convert(MemberRequest requestDto) {
        if (requestDto instanceof KakaoOauthMemberRequest) {
            return (KakaoOauthMemberRequest) requestDto;
        }
        throw new IllegalStateException();
    }
}
