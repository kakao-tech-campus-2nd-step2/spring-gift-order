/*
package gift.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${token.expire-length}")
    private long validityInMilliseconds;

    @Value("${jwt.secret-key}")
    private String secretKeyString;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String getEmailFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String createToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", email);
        long now = System.currentTimeMillis();
        long validity = now + validityInMilliseconds;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(validity))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

 */