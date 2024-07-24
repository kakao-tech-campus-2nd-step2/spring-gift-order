package gift.exception.oauth2;

import gift.exception.ErrorResult;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.TimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class oAuth2ExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(oAuth2Exception.class)
    public ErrorResult authExHandle(oAuth2Exception e) {
        return new ErrorResult("인가코드 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(oAuth2TokenException.class)
    public ErrorResult tokenExHandle(oAuth2TokenException e) {
        return new ErrorResult("토큰 에러", e.getMessage());
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler({TimeoutException.class})
    public ErrorResult timeoutExHandle() {
        return new ErrorResult("타임아웃 에러", "시간 초과로 요청을 처리하지 못하였습니다.");
    }
}
