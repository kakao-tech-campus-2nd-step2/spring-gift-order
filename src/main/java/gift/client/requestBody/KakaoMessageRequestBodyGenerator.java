package gift.client.requestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.KakaoMessageTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class KakaoMessageRequestBodyGenerator {

    private final ObjectMapper objectMapper;
    private String message;

    public KakaoMessageRequestBodyGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        KakaoMessageTemplate template = new KakaoMessageTemplate(message);

        try {
            param.add("template_object", objectMapper.writeValueAsString(template));
        } catch (JsonProcessingException e) {
            Logger.getLogger(KakaoMessageRequestBodyGenerator.class.getName()).log(Level.WARNING, "ObjectMapper가 JSON 파싱에 실패");
        }

        return param;
    }
}
