package gift.order;

import gift.option.Option;
import gift.option.OptionService;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

    public MessageTemplate setMessage(OrderRequest orderRequest) {
        return new MessageTemplate(
            "text",
            orderRequest.getMessage(),
            new Link("")
        );

       /* var link = new LinkedMultiValueMap<String, String>();
        link.add("web_url", "");
        var body = new LinkedMultiValueMap<String, String>();
        body.add("object_type", "text");
        body.add("text", orderRequest.getMessage());
        body.add("link", link.toString());*/
    }

    public void sendMessage(OrderRequest orderRequest, String accessToken) {
        RestClient client = RestClient.builder().build();
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var body = setMessage(orderRequest);

        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + accessToken)
            .body(body) // request body
            .retrieve()
            .toEntity(String.class);
    }

}
