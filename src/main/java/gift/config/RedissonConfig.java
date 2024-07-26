package gift.config;

import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    private static final String REDISSON_HOST_PREFIX = "redis://";
    private static final String KAKAO_ACCESS_TOKEN_PREFIX = "kakao:access:";
    private static final String KAKAO_REFRESH_TOKEN_PREFIX = "kakao:refresh:";

    @Bean
    public RedissonClient redissonClient(@Value("${redis.host}") String host,
                                         @Value("${redis.port}") String port
    ) {
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port);
        return Redisson.create(config);
    }

    @Bean
    public RMapCache<Long, String> kakaoAccessMap(RedissonClient redissonClient) {
        return redissonClient.getMapCache(KAKAO_ACCESS_TOKEN_PREFIX);
    }

    @Bean
    public RMapCache<Long, String> kakaoRefreshMap(RedissonClient redissonClient) {
        return redissonClient.getMapCache(KAKAO_REFRESH_TOKEN_PREFIX);
    }
}
