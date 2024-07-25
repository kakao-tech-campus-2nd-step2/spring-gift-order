package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() ||
                response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String seriesName = "UNKNOWN_ERROR";

        HttpStatus statusCode = (HttpStatus) response.getStatusCode();
        if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            seriesName = HttpStatus.Series.SERVER_ERROR.name();
        } else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            seriesName = HttpStatus.Series.CLIENT_ERROR.name();
            if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
                seriesName = HttpStatus.NOT_FOUND.name();
            }
        }
    }
}
