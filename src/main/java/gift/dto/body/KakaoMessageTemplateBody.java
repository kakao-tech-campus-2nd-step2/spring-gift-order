package gift.dto.body;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.KakaoMessageTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.logging.Level;
import java.util.logging.Logger;

public class KakaoMessageTemplateBody {
    private String message;

    public KakaoMessageTemplateBody(String message) {
        this.message = message;
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        KakaoMessageTemplate template = new KakaoMessageTemplate(message);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            param.add("template_object", objectMapper.writeValueAsString(template));
        } catch (JsonProcessingException e) {
            Logger.getLogger(KakaoMessageTemplateBody.class.getName()).log(Level.WARNING, "ObjectMapper가 JSON 파싱에 실패");
        }

        return param;
    }
}
