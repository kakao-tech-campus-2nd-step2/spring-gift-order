package gift.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.config.KakaoProperties;
import gift.member.model.Member;
import gift.option.domain.Option;
import gift.option.domain.OrderResponse;
import gift.option.repository.OptionJpaRepository;
import gift.order.domain.Order;
import gift.order.domain.Token;
import gift.order.dto.Link;
import gift.order.dto.OrderRequest;
import gift.order.dto.TemplateObject;
import gift.order.repository.OrderJPARepository;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OptionJpaRepository optionJpaRepository;
    private final WishService wishService;
    private final OrderJPARepository orderJPARepository;
    private final RestClient restClient = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;

    public OrderService(OptionJpaRepository optionJpaRepository, WishService wishService, OrderJPARepository orderJPARepository, KakaoProperties kakaoProperties) {
        this.optionJpaRepository = optionJpaRepository;
        this.wishService = wishService;
        this.orderJPARepository = orderJPARepository;
        this.kakaoProperties = kakaoProperties;
    }

    public OrderResponse requestOrder(@Valid OrderRequest orderRequest) {
        // 주문할 때 상품 옵션과 수량을 받아온다.
        Long request_optionId = orderRequest.getOptionId();
        Long request_quantity = orderRequest.getQuantity();
        String request_message = "Please handle with care";

        Option option = optionJpaRepository.findById(request_optionId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 옵션은 존재하지 않음 : " + request_optionId));

        if (request_quantity > option.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] 주문 수량이 재고 수량을 초과함");
        }

        // 주문 로직
        option.subtract(request_quantity);
//        Long productId = option.getProduct().getId();
//        if (wishService.getWishByProductId(productId) != null) {
//            wishService.deleteWish(productId);
//        }

        // 주문 내역 저장
        orderJPARepository.save(new Order(orderRequest.getOptionId(), orderRequest.getQuantity(), LocalDateTime.now(), request_message));
        Long orderId = orderJPARepository.findByOptionId(request_optionId).getId();

        return new OrderResponse(orderId, request_optionId, request_quantity, LocalDateTime.now(), request_message);
    }

    public void sendKakaoMessage(OrderResponse orderResponse, String accessToken) throws JsonProcessingException {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 액세스 토큰입니다.");
        }

        // 액세스 토큰 로그 출력 (디버깅 용도)
        System.out.println("Access Token: " + accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        TemplateObject templateObject = new TemplateObject(
                "text",
                orderResponse.toString(),
                new Link("link")
        );
        String templateObjectJson = objectMapper.writeValueAsString(templateObject);
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        map.set("template_object", templateObjectJson);

        String response = restClient.post()
                .uri(kakaoProperties.getMessageToMeUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(map)
                .retrieve()
                .body(String.class);

        System.out.println("Kakao API Response: " + response);
    }
}
