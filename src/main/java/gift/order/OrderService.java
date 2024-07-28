package gift.order;

import com.google.gson.Gson;
import gift.option.Option;
import gift.option.OptionService;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;


    public OrderService(OrderRepository orderRepository,
        OptionService optionService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
    }

    public OrderInfo saveOrder(OrderRequest orderRequest) {
        Long optionId = orderRequest.getOptionId();
        Option option = optionService.getOption(optionId);
        option.subtract(orderRequest.getQuantity());

        return orderRepository.save(orderRequest.toEntity());
    }

    public LinkedMultiValueMap<String, String> setMessage(OrderRequest orderRequest) {

        LinkedMultiValueMap<String, String> template = new LinkedMultiValueMap<String, String>();
        MessageTemplate messageTemplate = new MessageTemplate(
            "text",
            orderRequest.getMessage(),
            new Link("")
        );

        Gson gson = new Gson();

        String templateObjectJson = gson.toJson(messageTemplate);
        template.add("template_object", templateObjectJson);

        return template;
    }

    public void sendMessage(OrderRequest orderRequest, String accessToken) {
        RestClient client = RestClient.builder().build();
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var template = setMessage(orderRequest);

        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + accessToken)
            .body(template) // request body
            .retrieve()
            .toEntity(String.class);
    }

}
