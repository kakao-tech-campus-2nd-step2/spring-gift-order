package gift.util;

import java.util.Base64;

public class TokenUtil {

    public static String generateToken(String email, String password) {
        return Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }

    public static String extractEmailFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String base64Credentials = token.substring(7);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            return credentials.split(":")[0];
        }
        throw new IllegalArgumentException("잘못된 토큰입니다.");
    }
}