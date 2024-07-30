package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.message.KakaoMessageTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MessageService {

    private final RestClient restClient;
    ObjectMapper mapper = new ObjectMapper();

    public MessageService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void kakaoTalk(String accessToken, KakaoMessageTemplate messageTemplate) {
        restClient.post()
            .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .body(
                "template_object={\"object_type\": \"" + messageTemplate.object_type()
                    + "\", \"text\": \"" + messageTemplate.text()
                    + "\", \"link\": {\"web_url\": \"" + messageTemplate.link().web_url()
                    + "\", \"mobile_web_url\": \"" + messageTemplate.link().mobile_web_url()
                    + "\"}, \"button_title\": \"" + messageTemplate.button_title() + "\"}")
            .retrieve();
    }
}
