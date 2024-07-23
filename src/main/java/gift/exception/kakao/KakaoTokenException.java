package gift.exception.kakao;

public class KakaoTokenException extends RuntimeException {

    ;

    public KakaoTokenException() {
        super();
    }

    public KakaoTokenException(String message) {
        super(message);
    }

    public KakaoTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public KakaoTokenException(Throwable cause) {
        super(cause);
    }

    protected KakaoTokenException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
