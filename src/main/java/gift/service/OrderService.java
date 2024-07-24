package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.member.Member;
import gift.dto.OrderDto;
import gift.exception.GiftException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.service.kakao.KakaoMessageRequest;
import gift.service.kakao.Oauth2TokenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static gift.exception.ErrorCode.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Transactional
@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final Oauth2TokenService oauth2TokenService;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository, RestTemplate restTemplate, ObjectMapper objectMapper, OrderRepository orderRepository, Oauth2TokenService oauth2TokenService) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.oauth2TokenService = oauth2TokenService;
    }

    public OrderDto processOrder(OrderDto dto) {
        Long optionId = dto.getOptionId();
        Long quantity = dto.getQuantity();
        Member member = dto.getMember();
        String accessToken = member.getAccessToken();
        String message = dto.getMessage();

        if (oauth2TokenService.isAccessTokenExpired(accessToken)) {
            oauth2TokenService.refreshAccessToken(member);
        }

        Option option = getOptionById(optionId);
        subtractOptionQuantity(option, quantity);
        removeMemberWish(member, option.getProduct());
        sendKakaoMessage(accessToken, message);

        Order order = new Order(option, quantity, message);
        orderRepository.save(order);

        return OrderDto.from(order);
    }

    private Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new GiftException(OPTION_NOT_FOUND));
    }

    private void subtractOptionQuantity(Option option, Long quantity) {
        option.subtract(quantity);
    }

    private void removeMemberWish(Member member, Product product) {
        wishRepository.findByMemberAndProduct(member, product)
                .ifPresent(wish -> wishRepository.delete(wish));
    }

    private void sendKakaoMessage(String accessToken, String message) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = createHeaders(accessToken);
        LinkedMultiValueMap<String, String> body = createRequestBody(message);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, POST, httpEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GiftException(SEND_KAKAO_MESSAGE_FAILED);
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private LinkedMultiValueMap<String, String> createRequestBody(String message) {
        KakaoMessageRequest.Link link = new KakaoMessageRequest.Link("https://example.com");
        KakaoMessageRequest request = new KakaoMessageRequest("text", message, link);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        try {
            String templateObject = objectMapper.writeValueAsString(request);
            body.add("template_object", templateObject);
        } catch (JsonProcessingException e) {
            throw new GiftException(JSON_PROCESSING_FAILED);
        }
        return body;
    }

}
