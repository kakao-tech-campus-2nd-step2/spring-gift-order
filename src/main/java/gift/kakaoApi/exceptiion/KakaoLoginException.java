package gift.kakaoApi.exceptiion;

public class KakaoLoginException extends RuntimeException {

    private final String error_code;

    public KakaoLoginException(String error_code, String errorDescription) {
        super(errorDescription);
        this.error_code = error_code;
    }

    public String getErrorCode() {
        return error_code;
    }
}
