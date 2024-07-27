package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Wish;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final RestClient restClient;
    private final JwtUtil jwtUtil;

    @Autowired
    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, WishlistRepository wishlistRepository, RestClient restClient, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishlistRepository = wishlistRepository;
        this.restClient = restClient;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, String token) {
        Option option = optionRepository.findById(orderRequest.optionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        if (option.getQuantity() < orderRequest.quantity()) {
            throw new IllegalArgumentException("Insufficient quantity");
        }

        option.setQuantity(option.getQuantity() - orderRequest.quantity());
        optionRepository.save(option);

        Claims claims = jwtUtil.extractClaims(token);
        Long memberId = Long.parseLong(claims.getSubject());

        Wish wish = wishlistRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId())
                .orElse(null);

        if (wish != null) {
            wishlistRepository.delete(wish);
        }

        Order order = new Order();
        order.setOption(option);
        order.setQuantity(orderRequest.quantity());
        order.setOrderDateTime(LocalDateTime.now());
        order.setMessage(orderRequest.message());
        orderRepository.save(order);

        sendOrderMessage(order, token);

        return new OrderResponse(order.getId(), option.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }

    private void sendOrderMessage(Order order, String token) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        String messageTemplate = "{\n" +
                "  \"object_type\": \"text\",\n" +
                "  \"text\": \"Order Details:\\nOption: " + order.getOption().getName() + "\\nQuantity: " + order.getQuantity() + "\\nMessage: " + order.getMessage() + "\",\n" +
                "  \"link\": {\n" +
                "    \"web_url\": \"http://localhost:8080/orders/" + order.getId() + "\",\n" +
                "    \"mobile_web_url\": \"http://localhost:8080/orders/" + order.getId() + "\"\n" +
                "  },\n" +
                "  \"button_title\": \"View Order\"\n" +
                "}";

        try {
            restClient
                    .post()
                    .uri(url)
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body("template_object=" + messageTemplate)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send Kakao message", e);
        }
    }
}
