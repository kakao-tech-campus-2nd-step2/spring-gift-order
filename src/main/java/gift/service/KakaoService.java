package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.config.KakaoAuthClient;
import gift.dto.KakaoInfoDto;
import gift.dto.KakaoTokenResponseDto;
import gift.model.member.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class KakaoService {

    private MemberRepository memberRepository;

    private final KakaoAuthClient kakaoAuthClient;

    public KakaoService(KakaoAuthClient kakaoAuthClient,MemberRepository memberRepository) {
        this.kakaoAuthClient = kakaoAuthClient;
        this.memberRepository = memberRepository;
    }

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoAuthClient.getAccessToken(code).block();
        return kakaoTokenResponseDto.accessToken();
    }

    public KakaoInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        KakaoInfoDto kakaoInfoDto = kakaoAuthClient.getUserInfo(accessToken);
        return kakaoInfoDto;
    }
    public Member registerOrGetKakaoMember(String email){
        Optional<Member> kakaoMember = memberRepository.findByEmail(email);
        if(kakaoMember.isEmpty()){
            String tempPassword = new SecureRandom().toString();
            Member newKakaoMember = new Member(email,tempPassword,"member");
            memberRepository.save(newKakaoMember);
        }
        return memberRepository.findByEmail(email).get();
    }

    public void kakaoDisconnect(String accessToken){
        kakaoAuthClient.kakaoDisconnect(accessToken);
    }
}