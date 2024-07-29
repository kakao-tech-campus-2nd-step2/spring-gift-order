package gift.service;

import gift.dto.KakaoTokenDto;
import gift.entity.KakaoToken;
import gift.repository.KakaoTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoTokenService {

    public final KakaoTokenRepository kakaoTokenRepository;

    @Autowired
    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public void saveToken(KakaoTokenDto kakaoTokenDto) {
        KakaoToken kakaoToken = new KakaoToken(
                kakaoTokenDto.getEmail(),
                kakaoTokenDto.getAccessToken(),
                kakaoTokenDto.getRefreshToken()
        );
        kakaoTokenRepository.save(kakaoToken);
    }

    public KakaoTokenDto getTokenByEmail(String email) {
        KakaoToken kakaoToken = kakaoTokenRepository.findById(email).orElse(null);
        if (kakaoToken == null) {
            return null;
        }
        return new KakaoTokenDto(
                kakaoToken.getEmail(),
                kakaoToken.getAccessToken(),
                kakaoToken.getRefreshToken()
        );
    }
}
