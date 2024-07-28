package gift.service;

import gift.domain.KakaoToken;
import gift.repository.KakaoTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoTokenService {

    @Autowired
    private KakaoTokenRepository kakaoTokenRepository;

    @Transactional
    public KakaoToken saveToken(Long memberId, String email, String accessToken) {
        KakaoToken kakaoToken = kakaoTokenRepository.findByMemberId(memberId);

        if (kakaoToken == null) {
            kakaoToken = new KakaoToken();
            kakaoToken.setMemberId(memberId);
            kakaoToken.setEmail(email);
        }

        kakaoToken.setAccessToken(accessToken);

        return kakaoTokenRepository.save(kakaoToken);
    }
}
