package gift.service;

import gift.dto.MemberDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDto register(MemberDto memberDto) {
        Member member = new Member(memberDto.getEmail(), memberDto.getPassword());
        Member savedMember = memberRepository.save(member);
        String token = TokenUtil.generateToken(member.getEmail(), member.getPassword());
        return new MemberDto(savedMember.getId(), savedMember.getEmail(), savedMember.getPassword(), token);
    }

    public MemberDto login(String email, String password) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(() -> new RuntimeException("잘못된 인증입니다."));

        if (member.checkPassword(password)) {
            String token = TokenUtil.generateToken(email, password);
            return new MemberDto(member.getId(), email, password, token);
        } else {
            throw new RuntimeException("잘못된 인증입니다.");
        }
    }
}
