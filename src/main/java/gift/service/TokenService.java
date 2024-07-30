package gift.service;

import java.util.Base64;

public class TokenService {

    public static String generateToken(String email, String password) {
        return Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }

    public static String extractEmailFromToken(String token) {
        String credentials = new String(Base64.getDecoder().decode(token));
        return credentials.split(":")[0];
    }

    public static String[] decodeToken(String token) {
        String credentials = new String(Base64.getDecoder().decode(token));
        return credentials.split(":");
    }
}