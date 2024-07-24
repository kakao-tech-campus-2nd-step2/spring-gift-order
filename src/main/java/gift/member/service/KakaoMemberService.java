package gift.member.service;

import gift.member.domain.KakaoMember;
import gift.member.domain.Member;
import gift.member.dto.MemberServiceDto;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.KakaoMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KakaoMemberService {
    private final KakaoMemberRepository kakaoMemberRepository;

    public KakaoMemberService(KakaoMemberRepository kakaoMemberRepository) {
        this.kakaoMemberRepository = kakaoMemberRepository;
    }


    public List<KakaoMember> getAllMembers() {
        return kakaoMemberRepository.findAll();
    }

    public KakaoMember getMemberById(String accessToken) {
        return kakaoMemberRepository.findById(accessToken)
                .orElseThrow(MemberNotFoundException::new);
    }

    public KakaoMember createMember(KakaoMember kakaoMember) {
        return kakaoMemberRepository.save(kakaoMember);
    }
}
