package gift.util;

import gift.config.JwtConfig;
import gift.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret().getBytes())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Invalid JWT signature");
            return false;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Expired JWT token");
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Invalid JWT token");
            return false;
        } catch (Exception e) {
            System.out.println("JWT validation failed");
            return false;
        }
    }

    public Long getUserId(String token) {
        try {
            Claims claims = extractClaims(token);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            System.out.println("Failed to extract user ID from JWT token");
            return null;
        }
    }
}
