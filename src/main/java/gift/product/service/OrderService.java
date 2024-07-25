package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.OrderDTO;
import gift.product.exception.InvalidIdException;
import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final RestClient client = RestClient.builder().build();
    private final OptionRepository optionRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(
        OptionRepository optionRepository,
        ObjectMapper objectMapper,
        JwtUtil jwtUtil,
        OrderRepository orderRepository) {
        this.optionRepository = optionRepository;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> orderProduct(String authorization, OrderDTO orderDTO) {
        System.out.println("[OrderService] orderProduct()");
        Member member = jwtUtil.parsingToken(authorization);
        Option option = optionRepository.findById(orderDTO.getOptionId()).orElseThrow(
            () -> new InvalidIdException(NOT_EXIST_ID)
        );
        option.subtractQuantity(orderDTO.getQuantity());
        Order order = new Order(
            optionRepository.findById(orderDTO.getOptionId())
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID)),
            orderDTO.getQuantity(),
            orderDTO.getMessage()
        );
        orderRepository.save(order);
        sendToMe(member.getPassword(), order);
        return createResponse(order);
    }

    public void sendToMe(String accessToken, Order order) {
        System.out.println("[OrderService] sendToMe()");
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        final var body = createBody(
            "[ 주문 내역 ]\n"
                + "옵션 명: " + order.getOption().getName() + "\n"
                + "수량: " + order.getQuantity() + "\n"
                + "메세지: " + order.getMessage()
            );
        client.post()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

    private @NotNull Map<String, Object> createResponse(Order order) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.getId());
        response.put("optionId", order.getOption().getId());
        response.put("quantity", order.getQuantity());
        response.put("orderDateTime", order.getOrderDateTime());
        response.put("message", order.getMessage());
        return response;
    }

    private @NotNull LinkedMultiValueMap<String, Object> createBody(String message) {
        var body = new LinkedMultiValueMap<String, Object>();
        try {
            String templateObject = objectMapper.writeValueAsString(Map.of(
                "object_type", "text",
                "text", message,
                "link", Map.of()
            ));
            body.add("template_object", templateObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return body;
    }

}
