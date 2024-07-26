package gift.service;

import gift.domain.KakaoToken;
import gift.repository.KakaoTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KakaoTokenService {

    private final KakaoTokenRepository kakaoTokenRepository;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public void saveKakaoToken(String userEmail, String token) {
        Optional<KakaoToken> existingToken = kakaoTokenRepository.findByUserEmail(userEmail);
        if (existingToken.isPresent()) {
            KakaoToken kakaoToken = existingToken.get();
            kakaoToken.setToken(token);
            kakaoTokenRepository.save(kakaoToken);
        } else {
            KakaoToken kakaoToken = new KakaoToken(userEmail, token);
            kakaoTokenRepository.save(kakaoToken);
        }
    }
    public String getTokenByEmail(String email) {
        Optional<KakaoToken> kakaoToken = kakaoTokenRepository.findByUserEmail(email);
        return kakaoToken.map(KakaoToken::getToken).orElse(null);
    }
}
