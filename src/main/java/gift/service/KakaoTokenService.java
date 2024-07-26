package gift.service;

import gift.common.exception.AuthenticationException;
import gift.repository.KakaoTokenRepository;
import gift.service.dto.KakaoTokenDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoTokenService {

    private final KakaoTokenRepository kakaoTokenRepository;
    private final KakaoApiCaller kakaoApiCaller;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository, KakaoApiCaller kakaoApiCaller) {
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.kakaoApiCaller = kakaoApiCaller;
    }

    public void saveToken(Long memberId, KakaoTokenDto kakaoTokenDto) {
        kakaoTokenRepository.saveAccessToken(memberId, kakaoTokenDto);
        kakaoTokenRepository.saveRefreshToken(memberId, kakaoTokenDto);
    }

    public void deleteAccessToken(Long memberId) {
        kakaoTokenRepository.deleteAccessToken(memberId);
    }

    public void deleteRefreshToken(Long memberId) {
        kakaoTokenRepository.deleteRefreshToken(memberId);
    }

    public String refreshIfAccessTokenExpired(Long memberId) {
        if (kakaoTokenRepository.existsAccessToken(memberId)) {
            return kakaoTokenRepository.getAccessToken(memberId);
        }
        if (kakaoTokenRepository.existsRefreshToken(memberId)) {
            String refreshToken = kakaoTokenRepository.getRefreshToken(memberId);
            KakaoTokenDto tokenDto = kakaoApiCaller.refreshAccessToken(refreshToken);
            return tokenDto.access_token();
        }
        throw new AuthenticationException("Login has expired");
    }
}
