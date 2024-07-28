package gift.service;

import gift.dto.KakaoProperties;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;

    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public OrderService(KakaoProperties kakaoProperties,
        OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, RestTemplateBuilder restTemplateBuilder) {
        this.kakaoProperties = kakaoProperties;
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(3))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();
    }

    public OrderResponse addOrder(OrderRequest orderRequest, String token) {
        Option option = optionRepository.findById(orderRequest.getOptionId())
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        if (option.getQuantity() < orderRequest.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        option.subtractQuantity(orderRequest.getQuantity());
        optionRepository.save(option);

        Order order = new Order(orderRequest.getOptionId(), orderRequest.getQuantity(),
            LocalDateTime.now(), orderRequest.getMessage());
        order = orderRepository.save(order);

        wishRepository.deleteByOptionId(orderRequest.getOptionId());

        sendOrderMessage(order, token);

        return new OrderResponse(order.getId(), order.getOptionId(), order.getQuantity(),
            order.getOrderDateTime(), order.getMessage());
    }

    private void sendOrderMessage(Order order, String token) {
        String messageTemplate = createMessageTemplate(order);

        String url = KAKAO_API_URI + "/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", messageTemplate);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("카카오톡 메시지 전송 실패. 상태 코드: " + response.getStatusCode());
        }
        System.out.println("카카오톡 메시지가 성공적으로 전송되었습니다!");
    }

    private String createMessageTemplate(Order order) {
        Option option = optionRepository.findById(order.getOptionId())
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = order.getOrderDateTime().format(formatter);

        return "{ " +
            "\"object_type\": \"text\", " +
            "\"text\": \"주문 내역\\n" +
            "옵션명: " + option.getName() + "\\n" +
            "주문 메세지: " + order.getMessage() + "\\n" +
            "주문 수량: " + order.getQuantity() + "\\n" +
            "주문 시각: " + formattedDateTime + "\\n" +
            "남은 수량: " + option.getQuantity() + "\", " +
            "\"link\": { " +
            "\"web_url\": \"" + kakaoProperties.getRedirectUrl() + order.getId() + "\" " +
            "} " +
            "}";
    }

}
