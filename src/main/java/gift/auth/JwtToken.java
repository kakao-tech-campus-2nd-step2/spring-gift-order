package gift.auth;

import gift.auth.domain.JWT;
import gift.auth.domain.Token;
import gift.entity.UserEntity;
import gift.entity.enums.SocialType;
import gift.util.errorException.BaseHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtToken {

    private String secretKey;

    public JwtToken(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Token createToken(JWT jwt) {
        Claims claims = Jwts.claims();
        claims.put("id", jwt.getId());
        claims.put("email", jwt.getEmail());
        claims.put("socialToken", jwt.getSocialToken());
        claims.put("socialType", jwt.getSocialType());

        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime expirationDateTime = now.plusSeconds(jwt.getExp());

        return new Token(Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(expirationDateTime.toInstant()))
            .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
            .compact());
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다.");
        }
    }

    public Long getId(String token) {
        Claims claims = validateToken(token);
        return claims.get("id", Long.class);
    }

    public String getEmail(String token) {
        Claims claims = validateToken(token);
        return claims.get("email", String.class);
    }
    public String getSocialToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("socialToken", String.class);
    }
    public SocialType getSocialType(String token) {
        Claims claims = validateToken(token);
        return SocialType.valueOf(claims.get("socialType", String.class));
    }
}
