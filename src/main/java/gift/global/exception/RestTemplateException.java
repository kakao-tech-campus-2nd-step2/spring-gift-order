package gift.global.exception;

public class RestTemplateException extends RuntimeException {
    public RestTemplateException() {
        super("REST TEMPLATE 동작 실패");
    }
}
