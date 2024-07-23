package gift.exception;

import ch.qos.logback.core.spi.ErrorCodes;

public class KakaoException extends RuntimeException{
    private ErrorCode errorCode;
    private String detailMessage;

    public KakaoException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
