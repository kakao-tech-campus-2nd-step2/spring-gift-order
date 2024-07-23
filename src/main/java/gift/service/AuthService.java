package gift.service;

import gift.domain.Member;
import gift.dto.request.MemberRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.exception.customException.EmailDuplicationException;
import gift.exception.customException.MemberNotFoundException;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponseDto memberJoin(MemberRequestDto memberRequestDto){
        Member member = new Member.Builder()
                .email(memberRequestDto.email())
                .password(memberRequestDto.password())
                .build();

        Optional<Member> memberByEmail = memberRepository.findMemberByEmail(memberRequestDto.email());

        if(memberByEmail.isPresent()){
            throw new EmailDuplicationException();
        }

        Member savedMember = memberRepository.save(member);

        return MemberResponseDto.from(savedMember);
    }

    @Transactional
    public MemberResponseDto kakaoMemberLogin(String kakaoId){
        return memberRepository.findMemberByKakaoId(kakaoId).map(MemberResponseDto::from)
                .orElseGet(() -> memberKakaoJoin(kakaoId));
    }

    @Transactional
    public MemberResponseDto memberKakaoJoin(String kakaoId){
        Member member = new Member.Builder()
                .email(kakaoId+"@kakao.com")
                .password(kakaoId+"@kakao.com")
                .kakaoId(kakaoId)
                .build();

        Member savedMember = memberRepository.save(member);

        return MemberResponseDto.from(savedMember);
    }

    public MemberResponseDto findOneByEmailAndPassword(MemberRequestDto memberRequestDto){
        Member findMember = memberRepository.findMemberByEmailAndPassword(memberRequestDto.email(), memberRequestDto.password())
                .orElseThrow(MemberNotFoundException::new);

        return MemberResponseDto.from(findMember);
    }

}
