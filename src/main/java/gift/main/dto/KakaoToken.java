package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoToken(
        String accessToken,        //사용자 액세스 토큰 값
        String tokenType,          //토큰타입, bearer로 고정
        String refreshToken    //사용자 리프레시 토큰 값

) {
}
