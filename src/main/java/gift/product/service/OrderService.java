package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.OrderDTO;
import gift.product.exception.InvalidIdException;
import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.repository.OptionRepository;
import gift.product.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    public OrderService(
        OptionRepository optionRepository,
        ObjectMapper objectMapper,
        JwtUtil jwtUtil
    ) {
        this.optionRepository = optionRepository;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> orderProduct(String authorization, OrderDTO orderDTO) {
        System.out.println("[OrderService] orderProduct()");
        Member member = jwtUtil.parsingToken(authorization);
        Option option = optionRepository.findById(orderDTO.getOptionId()).orElseThrow(
            () -> new InvalidIdException(NOT_EXIST_ID)
        );
        option.subtractQuantity(orderDTO.getQuantity());
        sendToMe(member.getPassword(), option.getName(), orderDTO.getQuantity());
        return createResponse(orderDTO);
    }

    public void sendToMe(String accessToken, String optionName, int quantity) {
        System.out.println("[OrderService] sendToMe()");
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        final var body = createBody(
            "[ 주문 내역 ]\n"
                + "옵션 명: " + optionName + "\n"
                + "수량: " + quantity
            );
        client.post()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

    private @NotNull Map<String, String> createResponse(OrderDTO orderDTO) {
        Map<String, String> response = new HashMap<>();
        response.put("id", orderDTO.getOptionId().toString());
        response.put("quantity", String.valueOf(orderDTO.getQuantity()));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        response.put("orderDateTime", now.format(formatter));
        response.put("message", orderDTO.getMessage());
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
