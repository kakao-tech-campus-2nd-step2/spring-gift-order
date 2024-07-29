package gift.exceptionHandler;

import gift.constants.ErrorMessage;
import gift.exception.KakaoLoginBadRequestException;
import gift.exception.KakaoLoginForbiddenException;
import gift.exception.KakaoLoginUnauthorizedException;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

public class RestClientErrorHandler {

    public static ErrorHandler http4xxErrorHandler = (req, res) -> {
        switch (res.getStatusCode().value()) {
            case 400:
                throw new KakaoLoginBadRequestException(
                    ErrorMessage.KAKAO_BAD_REQUEST_MSG);
            case 401:
                throw new KakaoLoginUnauthorizedException(
                    ErrorMessage.KAKAO_UNAUTHORIZED_MSG);
            case 403:
                throw new KakaoLoginForbiddenException(
                    ErrorMessage.KAKAO_FORBIDDEN_MSG);
        }
    };
}
