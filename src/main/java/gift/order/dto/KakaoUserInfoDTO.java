package gift.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfoDTO {

    private long id;
    private LocalDateTime conectedAt;
    private KakaoAccountDTO kakaoAccount;

    public KakaoAccountDTO getKakaoAccount() {
        return kakaoAccount;
    }
}