package gift.service;

import gift.domain.other.Member;
import gift.domain.other.MemberRequest;
import gift.domain.other.WishList;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.NoSuchElementException;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private JwtService jwtService;

    public MemberService(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    public void join(MemberRequest memberRequest) {
        if (!memberRepository.existsById(memberRequest.id())) {
            memberRepository.save(new Member(memberRequest.id(), memberRequest.password(), memberRequest.name(),new LinkedList<WishList>()));
            return;
        }
        throw new NoSuchElementException("이미 존재하는 회원입니다.");
    }

    public String login(MemberRequest memberRequest) {
        Member dbMember = findById(memberRequest.id());
        dbMember.validatePassword(memberRequest.password());
        return jwtService.createJWT(dbMember.getId());
    }

    public Member findById(String Id) {
        return memberRepository.findById(Id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원 정보가 없습니다."));
    }
}
