package gift.order;

import gift.option.Option;
import gift.option.OptionRepository;
import gift.option.OptionResponse;
import gift.product.Product;
import gift.user.KakaoUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Date;

@Service
public class OrderService {

    private final RestClient kakaoRestClient;

    private final OptionRepository optionRepository;

    public OrderService(RestClient kakaoRestClient, OptionRepository optionRepository) {
        this.kakaoRestClient = kakaoRestClient;
        this.optionRepository = optionRepository;
    }

    public OrderResponse sendMessage(KakaoUser user, OrderRequest request, OptionResponse optionResponse) {
        String accessToken = user.getAccessToken();
        System.out.println(accessToken);
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        LinkedMultiValueMap<String, Object> body = createBody(request, optionResponse);

        ResponseEntity<String> response = kakaoRestClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class);

        return new OrderResponse(1L, request.optionId, request.quantity, new Date().toString(), request.getMessage());
    }

    private LinkedMultiValueMap<String, Object> createBody(OrderRequest request, OptionResponse optionResponse) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        //Link link = new Link("http://localhost:8080","http://localhost:8080");
        //Content content = new Content("주문이 완료되었습니다.", request.getMessage(), "imgurl", link);
        Option option = optionRepository.findById(optionResponse.id()).orElseThrow();
        Product product = option.getProduct();
        //ItemContent itemContent = new ItemContent(product.getName(), product.getImageUrl(), option.getName());
        //TemplateObject template_object = new TemplateObject("feed", content, itemContent);
        //TemplateObject template_object = new TemplateObject("text", request.getMessage(), link);
        //body.add("template_object", template_object);

        String send = "{\n" +
                "        \"object_type\": \"feed\",\n" +
                "        \"content\": {\n" +
                "            \"title\": \"주문이 완료되었습니다.\",\n" +
                "            \"description\": \"" + request.getMessage() + "\",\n" +
                "            \"image_url\": \"https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg\",\n" +
                "            \"link\": {\n" +
                "                \"web_url\": \"http://www.daum.net\",\n" +
                "                \"mobile_web_url\": \"http://m.daum.net\"\n" +
                "            }\n" +
                "           }," +
                "            \"item_content\" : {\n" +
                "                \"title_image_url\" : \"" + product.getImageUrl() + "\",\n" +
                "                \"title_image_text\" :\"" + product.getName() + "\",\n" +
                "                \"title_image_category\" : \"" + option.getName() + "\"" +
                "           }" +
                "       }";


        body.add("template_object", send);

        return body;
    }
}
