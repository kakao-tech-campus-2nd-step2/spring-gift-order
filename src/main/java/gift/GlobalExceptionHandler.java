package gift;

import gift.product.ResponseDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex){
        HashMap<String, String> map = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> {
            map.put(err.getField(), err.getDefaultMessage());
        });

        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                map
        );
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleAuthenticationException(JwtException ex){
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.UNAUTHORIZED,
                "JwtException",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleArgumentException(IllegalArgumentException ex){
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.BAD_REQUEST,
                "IllegalArgumentException",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex){
        if(ex.getMessage().startsWith("kakao")){
            int status = Integer.parseInt(ex.getMessage().split(" ")[1]);
            System.out.println(ex.getMessage());

            ResponseDTO responseDTO = new ResponseDTO(
                    HttpStatus.valueOf(status),
                    getMessage(status),
                    null
            );
            return ResponseEntity.status(HttpStatus.valueOf(status)).body(responseDTO);

        }
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.BAD_REQUEST,
                "RuntimeException",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);

    }

    private static String getMessage(int status) {
        String message = "카카오 로그인 오류 발생 - ";
        if(status == 400){
            message += "API의 필수 파라미터 관련 오류가 발생했습니다.";
        }
        if(status==401){
            message += "인증 관련 오류가 발생했습니다.";
        }
        if(status==403){
            message += "권한 오류가 발생했습니다.";
        }
        if(status==500){
            message += "서버 관련 시스템 오류가 발생했습니다.";
        }
        if(status==502){
            message += "게이트웨이 또는 프로토콜 등의 통신 오류가 발생했습니다.";
        }
        if(status==503){
            message += "서비스 점검중 입니다.";
        }
        return message;
    }

}
