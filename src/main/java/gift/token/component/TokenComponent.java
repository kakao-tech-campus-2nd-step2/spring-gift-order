package gift.token.component;

import gift.global.dto.TokenDto;
import gift.token.model.TokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

// JWT 토큰을 생성해주는 bean 클래스.
// 슬랙의 글을 참고하여, util보다는 bean이 적합할 것 같아 bean으로 생성
@Component
public class TokenComponent extends TokenManager {

    public TokenComponent() {
        super();
    }

    // 입력한 정보를 토대로 토큰을 반환하는 함수 (클래스끼리의 이동이므로 dto로 전달)
    public TokenDto getToken(long userId, String email, boolean isAdmin) {
        long currentTime = System.currentTimeMillis();
        // 유효기간은 60분
        long expirationTime = minuteToMillis(60);
        Date currentDate = new Date(currentTime);
        Date expirationDate = new Date(currentTime + expirationTime);

        String onlyToken = Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("email", email)
            .claim("isAdmin", isAdmin)
            .issuedAt(currentDate)
            .expiration(expirationDate)
            .signWith(secretKey)
            .compact();

        // 인증 방식 + 토큰으로 반환
        String token = getFullToken(onlyToken);

        return new TokenDto(token);
    }

    // secretKey를 기반으로 토큰 디코딩하는 함수
    public void validateToken(String token) {
        String onlyToken = getOnlyToken(token);

        try {
            // deprecated라고 해서 parserBuilder()를 사용하려고 하니 찾을 수가 없었습니다.
            // gradle에서 주입해줘야 하는 것 같은데, 수정하면 안 될 것 같아서 일단 parser()를 사용했습니다.
            Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes()))
                .build()
                .parseSignedClaims(onlyToken);

            boolean isExpired = claims
                .getPayload()
                .getExpiration()
                .before(new Date());

            if (isExpired) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "다시 로그인 필요");
            }
        } catch (Exception e) {
            // 서명이 잘못됐거나, 토큰이 잘못됐거나, 유효기간이 지났거나 등등의 이유로 다양한 예외가 발생할 수 있으므로 Exception으로 잡은 후에 401을 반환하겠습니다.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "다시 로그인 필요");
        }
    }

    private String getFullToken(String tokenOnly) {
        return BEARER + tokenOnly;
    }


    // minute을 넣으면 밀리초로 반환하는 메서드
    private long minuteToMillis(int minute) {
        return minute * 60L * 1000;
    }
}
