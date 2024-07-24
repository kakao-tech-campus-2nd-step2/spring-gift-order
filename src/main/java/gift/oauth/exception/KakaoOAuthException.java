package gift.oauth.exception;

public class KakaoOAuthException extends RuntimeException{
    private final String error_code;

    public KakaoOAuthException(String error_code, String errorDescription) {
        super(errorDescription);
        this.error_code = error_code;
    }

    public String getErrorCode() {
        return error_code;
    }
}
