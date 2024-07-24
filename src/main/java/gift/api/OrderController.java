package gift.api;

import gift.KakaoProperties;
import gift.service.OptionService;
import gift.service.ProductService;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class OrderController {

    private final OptionService optionService;
    private final ProductService productService;
    private final KakaoProperties kakaoProperties;
    private final WebClient webClient;

    public OrderController(OptionService optionService, ProductService productService, KakaoProperties kakaoProperties, WebClient webClient) {
        this.optionService = optionService;
        this.productService = productService;
        this.kakaoProperties = kakaoProperties;
        this.webClient = webClient;
    }

    @PostMapping("/api/orders")
    public ResponseEntity<String> createOrder(@RequestHeader("Authorization") String authorization, @RequestBody OrderRequest orderRequest) {
        // Bearer token 추출
        String token = authorization.replace("Bearer ", "");

        // Option 수량 차감
        boolean updated = optionService.decreaseOptionQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());

        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient product quantity.");
        }

        // 카카오톡 메시지 전송
        boolean messageSent = sendKakaoMessage(token, orderRequest);

        if (messageSent) {
            return ResponseEntity.ok("Order created and message sent.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order created but failed to send message.");
        }
    }

    private boolean sendKakaoMessage(String accessToken, OrderRequest orderRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String productName = productService.getProductNameById(orderRequest.getProductId());
        String optionName = optionService.getOptionNameById(orderRequest.getOptionId());
        int remainingQuantity = optionService.getRemainingQuantityById(orderRequest.getOptionId());

        String messageContent = String.format(
            "Order Details:\nProduct: %s\nOption: %s\nQuantity: %d\nMessage: %s\nRemaining Quantity: %d",
            productName, optionName, orderRequest.getQuantity(), orderRequest.getMessage(), remainingQuantity
        );

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("template_object", createMessagePayload(messageContent));

        return webClient.post()
            .uri(kakaoProperties.getSendMessageUrl())
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .body(BodyInserters.fromFormData(parameters))
            .retrieve()
            .onStatus(status -> status.isError(), response -> {
                // 로그 추가 또는 처리
                return Mono.error(new RuntimeException("Error while sending Kakao message"));
            })
            .bodyToMono(String.class)
            .map(response -> true)
            .onErrorReturn(false)
            .block();
    }

    private String createMessagePayload(String messageContent) {
        JSONObject payload = new JSONObject();
        payload.put("object_type", "text");
        payload.put("text", messageContent);
        JSONObject link = new JSONObject();
        link.put("web_url", "");
        link.put("mobile_web_url", "");
        payload.put("link", link);
        payload.put("button_title", "버튼");
        return payload.toString();
    }
}