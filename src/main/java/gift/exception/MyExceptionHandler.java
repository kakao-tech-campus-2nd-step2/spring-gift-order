package gift.exception;

import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.exception.exception.ServerInternalException;
import gift.exception.exception.UnAuthException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class MyExceptionHandler {
    public static String back(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("history.back();");
        sb.append("</script>");
        return sb.toString();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public String NotFound(NotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(ServerInternalException.class)
    public String ServerInternal(ServerInternalException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UnAuthException.class)
    public String UnAuthorized(UnAuthException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public String BadRequest(BadRequestException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public String ConstaintViolation(ConstraintViolationException e) {
        return "제약사항 요구 어긋남";
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    public String ExpiredJwt(ExpiredJwtException e) {
        return "인증이 잘못됨";
    }
}

