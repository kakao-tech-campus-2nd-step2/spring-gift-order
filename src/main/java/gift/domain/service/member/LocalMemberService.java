package gift.domain.service.member;

import gift.domain.dto.request.member.LocalMemberRequest;
import gift.domain.dto.request.member.MemberRequest;
import gift.domain.dto.response.MemberResponse;
import gift.domain.entity.LocalMember;
import gift.domain.entity.Member;
import gift.domain.exception.forbidden.MemberIncorrectLoginInfoException;
import gift.domain.exception.notFound.MemberNotFoundException;
import gift.domain.repository.LocalMemberRepository;
import gift.global.util.HashUtil;
import gift.global.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalMemberService implements DerivedMemberService<LocalMember, LocalMemberRequest> {

    private final LocalMemberRepository localMemberRepository;
    private final JwtUtil jwtUtil;

    public LocalMemberService(LocalMemberRepository localMemberRepository, JwtUtil jwtUtil) {
        this.localMemberRepository = localMemberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public MemberResponse registerMember(MemberRequest requestDto, Member member) {
        LocalMember localMember = localMemberRepository.save(convert(requestDto).toEntity(member));
        member.setLocalMember(localMember);
        return new MemberResponse(jwtUtil.generateToken(member));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse loginMember(MemberRequest requestDto, Member member) {
        LocalMember localMember = findDerivedMemberBy(member);

        // 유저는 존재하나 비밀번호가 맞지 않은 채 로그인 시도
        if (!HashUtil.hashCode(convert(requestDto).getPassword()).equals(localMember.getPassword())) {
            throw new MemberIncorrectLoginInfoException();
        }

        return new MemberResponse(jwtUtil.generateToken(member));
    }

    @Override
    @Transactional(readOnly = true)
    public LocalMember findDerivedMemberBy(Member member) {
        return localMemberRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public LocalMemberRequest convert(MemberRequest requestDto) {
        if (requestDto instanceof LocalMemberRequest) {
            return (LocalMemberRequest) requestDto;
        }
        throw new IllegalStateException();
    }
}
