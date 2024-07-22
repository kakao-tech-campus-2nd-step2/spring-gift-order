package gift.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gift.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    private final ObjectMapper objectMapper;

    public ExceptionControllerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e) {
        ErrorCode error = ErrorCode.VALIDATION_ERROR;

        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .code(error.getStatus().value())
                .message(error.getMessage())
                .build();

        e.getFieldErrors().forEach(fieldError -> response.addValidation(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(GiftException.class)
    public ResponseEntity<ErrorResponse> giftException(GiftException e) {
        HttpStatus status = e.getErrorMessage().getStatus();

        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .code(status.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(status)
                .body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<KakaoErrorResponse> kakaoLoginException(HttpClientErrorException e) throws JsonProcessingException {
        String json = extractJsonFromMessage(e.getMessage());
        HttpStatusCode statusCode = e.getStatusCode();

        KakaoErrorResponse kakaoErrorResponse = objectMapper.readValue(json, KakaoErrorResponse.class);

        return ResponseEntity.status(statusCode)
                .body(kakaoErrorResponse);
    }

    private String extractJsonFromMessage(String message) {
        Pattern pattern = Pattern.compile("\\{.*?\\}");
        Matcher matcher = pattern.matcher(message);

        String jsonString = matcher.results()
                .map(MatchResult::group)
                .findFirst().orElse("{}");
        return jsonString;
    }

}
