package gift.exception;

import org.springframework.http.HttpStatus;

public class OauthLoginException extends CustomException {

    public OauthLoginException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
