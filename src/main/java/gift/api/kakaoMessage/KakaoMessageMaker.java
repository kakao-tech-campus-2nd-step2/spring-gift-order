package gift.api.kakaoMessage;

import com.google.gson.Gson;
import gift.order.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
public class KakaoMessageMaker {

    private final Gson gson;

    public KakaoMessageMaker(Gson gson) {
        this.gson = gson;
    }

    public LinkedMultiValueMap createOrderMessage(Order order) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        TextTemplate textTemplate = new TextTemplate("text", order.createOrderText(),
            new Link("http://localhost:8080", "http://localhost:8080"));
        body.add("template_object", gson.toJson(textTemplate));
        return body;
    }
}
