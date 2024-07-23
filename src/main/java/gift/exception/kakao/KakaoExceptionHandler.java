package gift.exception.kakao;

import gift.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KakaoExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(KakaoAuthException.class)
    public ErrorResult authExHandle(KakaoAuthException e) {
        return new ErrorResult("인가코드 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(KakaoTokenException.class)
    public ErrorResult tokenExHandle(KakaoTokenException e) {
        return new ErrorResult("토큰 에러", e.getMessage());
    }
}
