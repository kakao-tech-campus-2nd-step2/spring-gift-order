package gift.service;

import gift.KakaoProperties;
import gift.api.OrderRequest;
import gift.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OptionService optionService;
    private final ProductService productService;
    private final KakaoProperties kakaoProperties;
    private final WebClient webClient;
    private final UserService userService;
    private final WishListService wishListService;
    private final KakaoService kakaoService;

    public OrderService(OptionService optionService, ProductService productService, KakaoProperties kakaoProperties, WebClient webClient, UserService userService, WishListService wishListService, KakaoService kakaoService) {
        this.optionService = optionService;
        this.productService = productService;
        this.kakaoProperties = kakaoProperties;
        this.webClient = webClient;
        this.userService = userService;
        this.wishListService = wishListService;
        this.kakaoService = kakaoService;
    }

    public String createOrder(String token, OrderRequest orderRequest) {
        // Option 수량 차감
        boolean updated = optionService.decreaseOptionQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());

        if (!updated) {
            throw new IllegalArgumentException("Insufficient product quantity.");
        }

        // 카카오톡 메시지 전송
        boolean messageSent = sendKakaoMessage(token, orderRequest);

        if (!messageSent) {
            throw new RuntimeException("Order created but failed to send message.");
        }

        // access token에서 이메일 추출
        String email = kakaoService.getUserEmail(token);

        // 이메일로 사용자 조회
        User user = userService.findByEmail(email);
        if (user != null && wishListService.isProductInWishList(email, orderRequest.getProductId())) {
            // 위시리스트에서 주문한 제품 ID 삭제
            wishListService.removeProductFromWishList(email, orderRequest.getProductId());
        }

        return "Order created and message sent.";
    }

    private boolean sendKakaoMessage(String accessToken, OrderRequest orderRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String productName = productService.getProductNameById(orderRequest.getProductId());
        String optionName = optionService.getOptionNameById(orderRequest.getOptionId());
        // 현재 시각을 포맷팅
        LocalDateTime orderDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = orderDateTime.format(formatter);

        String messageContent = String.format(
            "Order Details:\nProduct: %s\nOption: %s\nQuantity: %d\nMessage: %s\nOrder DateTime: %s",
            productName, optionName, orderRequest.getQuantity(), orderRequest.getMessage(), formattedDateTime
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