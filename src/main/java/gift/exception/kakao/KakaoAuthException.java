package gift.exception.kakao;

public class KakaoAuthException extends RuntimeException {

    public KakaoAuthException() {
        super();
    }

    public KakaoAuthException(String error, String errorDescription) {
        super(String.format("Error: %s, Description: %s", error, errorDescription));
    }

    public KakaoAuthException(String message) {
        super(message);
    }

    public KakaoAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public KakaoAuthException(Throwable cause) {
        super(cause);
    }

    protected KakaoAuthException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
