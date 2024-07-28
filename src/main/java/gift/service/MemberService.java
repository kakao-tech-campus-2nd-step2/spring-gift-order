package gift.service;

import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void registerOrUpdateMember(String kakaoId, String nickname) {
        Member member = memberRepository.findByEmail(kakaoId)
                .orElse(new Member());
        member.setEmail(kakaoId);
        member.setPassword(""); // 패스워드는 사용하지 않음
        memberRepository.save(member);
    }

    public Optional<Member> findByKakaoId(String kakaoId) {
        return memberRepository.findByEmail(kakaoId);
    }
}
