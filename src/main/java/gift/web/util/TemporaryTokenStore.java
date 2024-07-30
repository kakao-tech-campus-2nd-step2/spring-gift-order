package gift.web.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class TemporaryTokenStore {
    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    // 토큰 저장
    public void storeToken(String email, String token) {
        tokenStore.put(email, token);

        scheduleTokenRemove(email, 120L, TimeUnit.MINUTES);
    }

    private void scheduleTokenRemove(String email, Long delay, TimeUnit timeUnit) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                tokenStore.remove(email);
            }
        }, timeUnit.toMillis(delay));
    }

    public String getToken(String email) {
        return tokenStore.get(email);
    }

    public void removeToken(String email) {
        tokenStore.remove(email);
    }
}
