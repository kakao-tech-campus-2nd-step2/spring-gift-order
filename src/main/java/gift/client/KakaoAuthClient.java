package gift.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoAuthClient {

    @PostExchange(value = "/oauth/token")
    JsonNode getAccessToken(@RequestBody MultiValueMap<String, String> body);
}
