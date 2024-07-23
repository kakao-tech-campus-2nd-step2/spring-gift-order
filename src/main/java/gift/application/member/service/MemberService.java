package gift.application.member.service;

import gift.global.auth.jwt.JwtProvider;
import gift.model.member.Member;
import gift.global.validate.InvalidAuthRequestException;
import gift.global.validate.NotFoundException;
import gift.repository.member.MemberRepository;
import gift.application.member.dto.MemberCommand;
import gift.application.member.dto.MemberModel;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public MemberModel.Info register(MemberCommand.Create command) {
        if (memberRepository.existsByEmail(command.email())) {
            throw new InvalidAuthRequestException("User already exists.");
        }
        var member = memberRepository.save(command.toEntity());
        return MemberModel.Info.from(member);
    }

    @Transactional(readOnly = true)
    public String login(MemberCommand.Login command) {
        Member member = memberRepository.findByEmail(command.email())
            .orElseThrow(() -> new NotFoundException("User not found."));

        if (!member.verifyPassword(command.password())) {
            throw new InvalidAuthRequestException("Password is incorrect.");
        }
        return jwtProvider.createToken(member.getId(), member.getRole());
    }

    @Transactional(readOnly = true)
    public MemberModel.Info getUser(Long memberId) {
        var member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("User not found."));
        return MemberModel.Info.from(member);
    }

    public String socialLogin(MemberCommand.Create create) {
        if (memberRepository.existsByEmail(create.email())) {
            var member = memberRepository.findByEmail(create.email())
                .orElseThrow(() -> new NotFoundException("User not found."));
            return jwtProvider.createToken(member.getId(), member.getRole());
        }

        var member = memberRepository.save(create.toEntity());
        return jwtProvider.createToken(member.getId(), member.getRole());
    }
}
