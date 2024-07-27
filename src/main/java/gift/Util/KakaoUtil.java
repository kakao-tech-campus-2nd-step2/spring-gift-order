package gift.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Link;
import gift.DTO.SendToMeTemplate;
import gift.Exception.JsonRunTimeException;
import gift.Model.Value.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoUtil {
    private static final String MESSAGE_TO_ME_URI = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String SEND_TO_ME_WEB_URL = "http://localhost:8080";

    private final RestClient client;

    public KakaoUtil(RestClient client) {
        this.client = client;
    }

    public void sendMessageToMe(AccessToken accessToken, String message){
        System.out.println("메세지 발송 도입");
        LinkedMultiValueMap<String, String> body = generateBodyOfSendMessageToMe(message);
        System.out.println(body);
            String str=client.post()
                    .uri(URI.create(MESSAGE_TO_ME_URI))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken.getValue())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class).getBody().toString();

            System.out.println("str:" + str);
    }

    private LinkedMultiValueMap<String, String> generateBodyOfSendMessageToMe(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        SendToMeTemplate templateObject = new SendToMeTemplate(
                "text",
                message,
                new Link(SEND_TO_ME_WEB_URL,SEND_TO_ME_WEB_URL));
        try {
            body.add("template_object", objectMapper.writeValueAsString(templateObject));
        } catch (JsonProcessingException e){
            throw new JsonRunTimeException("객체를 Json문자열로 변환 중 에러 발생");
        }
        return body;
    }
}
