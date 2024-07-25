package gift.repository;

import gift.config.RedisConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Import(RedisConfig.class)
class KakaoTokenRepositoryTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    @DisplayName("Redis 저장 테스트[성공]")
    void save() {
        // given
        String TokenMap1 = "token11";
        String TokenMap2 = "token22";
        Long memberId = 1L;
        String accessToken = "accessTOken";
        int duration = 100;

        // when
        RMapCache<Long, String> map  = redissonClient.getMapCache(TokenMap1);
        RMapCache<Long, String> ma2  = redissonClient.getMapCache(TokenMap2);
        map.put(memberId, accessToken, duration, TimeUnit.SECONDS);
        map.put(2L, accessToken, duration, TimeUnit.SECONDS);
        ma2.put(memberId, accessToken, duration, TimeUnit.SECONDS);
        // then
        assertThat(map.containsKey(memberId)).isTrue();
        assertThat(map).hasSize(2);
        assertThat(ma2).hasSize(1);
    }

    @Test
    @DisplayName("액세스 토큰 만료 테스트[성공]")
    void testRMapCacheExpiration() {
        // given
        String tokenMap = "token";
        Long memberId = 1L;
        String accessToken = "accessTOken";
        int duration = 5; // 5초 동안 유효

        // when
        RMapCache<Long, String> map = redissonClient.getMapCache(tokenMap);
        map.put(memberId, accessToken, duration, TimeUnit.SECONDS);

        // then
        assertThat(map.containsKey(memberId)).isTrue();
        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(map.containsKey(memberId)).isFalse();
        });
    }
}