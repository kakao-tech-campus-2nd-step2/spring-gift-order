package gift.exception.oauth2;

public class oAuth2TokenException extends RuntimeException {

    ;

    public oAuth2TokenException() {
        super();
    }

    public oAuth2TokenException(String message) {
        super(message);
    }

    public oAuth2TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public oAuth2TokenException(Throwable cause) {
        super(cause);
    }

    protected oAuth2TokenException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
