package gift.exception.customException;

import org.springframework.http.HttpStatusCode;

public class KakaoApiException extends RuntimeException {

    private final HttpStatusCode code;
    private final String message;

    public KakaoApiException(HttpStatusCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatusCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}