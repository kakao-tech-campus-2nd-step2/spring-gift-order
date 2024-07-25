package gift.repository;

import gift.service.dto.KakaoTokenDto;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class KakaoTokenRepository {

    private static final String KAKAO_ACCESS_TOKEN_PREFIX = "kakao:access:";
    private static final String KAKAO_REFRESH_TOKEN_PREFIX = "kakao:refresh:";

    private final RedissonClient redissonClient;

    public KakaoTokenRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void saveAccessToken(Long memberId, KakaoTokenDto tokenDto) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_ACCESS_TOKEN_PREFIX);
        map.put(memberId, tokenDto.access_token(), tokenDto.expires_in(), TimeUnit.SECONDS);
    }

    public void saveRefreshToken(Long memberId, KakaoTokenDto tokenDto) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_REFRESH_TOKEN_PREFIX);
        map.put(memberId, tokenDto.refresh_token(), tokenDto.refresh_token_expires_in(), TimeUnit.SECONDS);
    }

    public String getAccessToken(Long memberId) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_ACCESS_TOKEN_PREFIX);
        return map.get(memberId);
    }

    public String getRefreshToken(Long memberId) {
        RMapCache<Long, String> map = redissonClient.getMapCache(KAKAO_REFRESH_TOKEN_PREFIX);
        return map.get(memberId);
    }

    public void deleteAccessToken(Long memberId) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_ACCESS_TOKEN_PREFIX);
        map.fastRemove(memberId);
    }

    public void deleteRefreshToken(Long memberId) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_REFRESH_TOKEN_PREFIX);
        map.fastRemove(memberId);
    }

    public boolean existsAccessToken(Long memberId) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_ACCESS_TOKEN_PREFIX);
        return map.containsKey(memberId);
    }

    public boolean existsRefreshToken(Long memberId) {
        RMapCache<Long, String> map  = redissonClient.getMapCache(KAKAO_REFRESH_TOKEN_PREFIX);
        return map.containsKey(memberId);
    }
}
