package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.INVALID_HTTP_REQUEST;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.NOT_RECEIVE_RESPONSE;
import static gift.product.intercepter.AuthInterceptor.AUTHORIZATION_HEADER;
import static gift.product.intercepter.AuthInterceptor.BEARER_PREFIX;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.OrderDTO;
import gift.product.exception.InvalidIdException;
import gift.product.exception.RequestException;
import gift.product.exception.ResponseException;
import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;
    private final RestClient client = RestClient.builder()
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
            throw new RequestException(INVALID_HTTP_REQUEST);
        })
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
            throw new ResponseException(NOT_RECEIVE_RESPONSE);
        })
        .build();

    @Autowired
    public OrderService(
        OptionRepository optionRepository,
        ObjectMapper objectMapper,
        JwtUtil jwtUtil,
        OrderRepository orderRepository
    ) {
        this.optionRepository = optionRepository;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.orderRepository = orderRepository;
    }

    public Order orderProduct(String authorization, Long optionId, OrderDTO orderDTO) {
        System.out.println("[OrderService] orderProduct()");
        Member orderer = jwtUtil.parsingToken(authorization);
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        option.subtractQuantity(orderDTO.getQuantity());
        Order order = orderRepository.save(orderDTO.convertToDomain(option, orderer));
        if(orderer.getSnsMember() != null)
            sendToMe(order);
        return order;
    }

    public void sendToMe(Order order) {
        System.out.println("[OrderService] sendToMe()");
        final var body = createBody(
            "[ 주문 내역 ]"
                + "\n옵션 명: " + order.getOption().getName()
                + "\n수량: " + order.getQuantity()
                + "\n메세지: " + order.getMessage());
        String kakaoAccessToken = order.getOrderer().getSnsMember().getAccessToken();
        postRequest(kakaoAccessToken, body);
    }

    private @NotNull LinkedMultiValueMap<String, Object> createBody(String message) {
        var body = new LinkedMultiValueMap<String, Object>();
        try {
            body.add("template_object", objectMapper.writeValueAsString(
                Map.of(
                    "object_type", "text",
                    "text", message,
                    "link", Map.of(
                        "web_url", "",
                        "mobile_web_url", ""
                    )
                )
            ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return body;
    }

    private void postRequest(
        String kakaoAccessToken,
        LinkedMultiValueMap<String, Object> body) {
        client.post()
            .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
            .header(AUTHORIZATION_HEADER, BEARER_PREFIX + kakaoAccessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }
}
