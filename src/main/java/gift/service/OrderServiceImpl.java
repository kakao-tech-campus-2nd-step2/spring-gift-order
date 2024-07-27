package gift.service;

import gift.model.Order;
import gift.model.Option;
import gift.repository.OrderRepository;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WebClient webClient;

    public OrderServiceImpl(OrderRepository orderRepository, OptionRepository optionRepository, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.webClient = webClientBuilder.baseUrl("https://kapi.kakao.com").build();
    }

    @Override
    @Transactional
    public Order createOrder(Order order, String kakaoToken) {
        Option option = optionRepository.findById(order.getOption().getId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        option.subtractQuantity(order.getQuantity());
        optionRepository.save(option);

        order.setOrderDateTime(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        sendKakaoMessage(savedOrder, kakaoToken);

        return savedOrder;
    }

    private void sendKakaoMessage(Order order, String kakaoToken) {
        String message = "Order ID: " + order.getId() +
            "\nOption ID: " + order.getOption().getId() +
            "\nQuantity: " + order.getQuantity() +
            "\nMessage: " + order.getMessage();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", "{\"object_type\":\"text\",\"text\":\"" + message + "\"}");

        try {
            webClient.post()
                .uri("/v2/api/talk/memo/default/send")
                .header("Authorization", "Bearer " + kakaoToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send Kakao message: " + e.getMessage());
        }
    }
}