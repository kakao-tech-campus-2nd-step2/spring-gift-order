package gift.api.order;

import gift.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class MessageFailException extends GlobalException {

    public static final String MESSAGE = "Fail to send message.";

    public MessageFailException() {
        super(MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
