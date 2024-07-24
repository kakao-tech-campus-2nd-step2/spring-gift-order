package gift.exception.oauth2;

public class oAuth2Exception extends RuntimeException {

    public oAuth2Exception() {
        super();
    }

    public oAuth2Exception(String error, String errorDescription) {
        super(String.format("Error: %s, Description: %s", error, errorDescription));
    }

    public oAuth2Exception(String message) {
        super(message);
    }

    public oAuth2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public oAuth2Exception(Throwable cause) {
        super(cause);
    }

    protected oAuth2Exception(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
