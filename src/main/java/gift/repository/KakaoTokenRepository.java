package gift.repository;

import gift.service.dto.KakaoTokenDto;
import org.redisson.api.RMapCache;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class KakaoTokenRepository {

    private final RMapCache<Long, String> kakaoAccessMap;
    private final RMapCache<Long, String> kakaoRefreshMap;

    public KakaoTokenRepository(RMapCache<Long, String> kakaoAccessMap, RMapCache<Long, String> kakaoRefreshMap) {
        this.kakaoAccessMap = kakaoAccessMap;
        this.kakaoRefreshMap = kakaoRefreshMap;
    }

    public void saveAccessToken(Long memberId, KakaoTokenDto tokenDto) {
        kakaoAccessMap.put(memberId, tokenDto.access_token(), tokenDto.expires_in(), TimeUnit.SECONDS);
    }

    public void saveRefreshToken(Long memberId, KakaoTokenDto tokenDto) {
        kakaoRefreshMap.put(memberId, tokenDto.refresh_token(), tokenDto.refresh_token_expires_in(), TimeUnit.SECONDS);
    }

    public String getAccessToken(Long memberId) {
        return kakaoAccessMap.get(memberId);
    }

    public String getRefreshToken(Long memberId) {
        return kakaoRefreshMap.get(memberId);
    }

    public void deleteAccessToken(Long memberId) {
        kakaoAccessMap.fastRemove(memberId);
    }

    public void deleteRefreshToken(Long memberId) {
        kakaoRefreshMap.fastRemove(memberId);
    }

    public boolean existsAccessToken(Long memberId) {
        return kakaoAccessMap.containsKey(memberId);
    }

    public boolean existsRefreshToken(Long memberId) {
        return kakaoRefreshMap.containsKey(memberId);
    }
}
