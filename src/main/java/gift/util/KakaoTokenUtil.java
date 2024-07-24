package gift.util;

import gift.util.dto.KakaoTokenDto;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class KakaoTokenUtil {

    private static final String KAKAO_ACCESS_TOKEN_PREFIX = "kakao:access:";
    private static final String KAKAO_REFRESH_TOKEN_PREFIX = "kakao:refresh:";

    private final RedissonClient redissonClient;

    public KakaoTokenUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void saveToken(Long memberId, KakaoTokenDto kakaoTokenDto) {
        saveAccessToken(memberId, kakaoTokenDto);
        saveRefreshToken(memberId, kakaoTokenDto);
    }
    public void saveAccessToken(Long memberId, KakaoTokenDto tokenDto) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_ACCESS_TOKEN_PREFIX + memberId);
        bucket.set(tokenDto.access_token());
        bucket.expire(Instant.now().plusSeconds(tokenDto.expires_in()));
    }

    public void saveRefreshToken(Long memberId, KakaoTokenDto tokenDto) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_REFRESH_TOKEN_PREFIX + memberId);
        bucket.set(tokenDto.refresh_token());
        bucket.expire(Instant.now().plusSeconds(tokenDto.refresh_token_expires_in()));
    }

    public String getAccessToken(Long memberId) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_ACCESS_TOKEN_PREFIX + memberId);
        return bucket.get();
    }

    public String getRefreshToken(Long memberId) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_REFRESH_TOKEN_PREFIX + memberId);
        return bucket.get();
    }

    public void deleteAccessToken(Long memberId) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_ACCESS_TOKEN_PREFIX + memberId);
        bucket.delete();
    }

    public void deleteRefreshToken(Long memberId) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_REFRESH_TOKEN_PREFIX + memberId);
        bucket.delete();
    }

    public boolean existsAccessToken(Long memberId) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_ACCESS_TOKEN_PREFIX + memberId);
        return bucket.get() != null;
    }

    public boolean existsRefreshToken(Long memberId) {
        RBucket<String> bucket = redissonClient.getBucket(KAKAO_REFRESH_TOKEN_PREFIX + memberId);
        return bucket.get() != null;
    }
}
