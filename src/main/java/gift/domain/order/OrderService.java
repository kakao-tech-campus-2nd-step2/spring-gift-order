package gift.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.cartItem.JpaCartItemRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionService;
import gift.domain.product.Product;
import gift.domain.user.JpaUserRepository;
import gift.domain.user.User;
import gift.domain.user.dto.UserInfo;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final String SEND_ME_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final JpaOptionRepository optionRepository;
    private final OptionService optionService;
    private final JpaCartItemRepository cartItemRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JpaUserRepository userRepository;

    @Autowired
    public OrderService(
        JpaOptionRepository jpaOptionRepository,
        OptionService optionService,
        JpaCartItemRepository jpaCartItemRepository,
        RestTemplateBuilder restTemplateBuilder,
        ObjectMapper objectMapper,
        JpaUserRepository userRepository
    ) {
        optionRepository = jpaOptionRepository;
        this.optionService = optionService;
        cartItemRepository = jpaCartItemRepository;
        restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    /**
     * (나에게) 상품 선물하기
     */
    @Transactional
    public void order(OrderRequestDTO orderRequestDTO, UserInfo userInfo) {
        // 해당 상품의 옵션의 수량을 차감
        optionService.decreaseOptionQuantity(orderRequestDTO.optionId(), orderRequestDTO.quantity());

        // 해당 상품이 (나의) 위시리스트에 있는 경우 위시 리스트에서 삭제
        removeFromWishList(userInfo.getId(), orderRequestDTO.optionId());

        // 메세지 작성
        MultiValueMap<String, String> body = createTemplateObject(
            orderRequestDTO);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        User user = userRepository.findById(userInfo.getId()).get();
        headers.setBearerAuth(user.getAccessToken()); // 엑세스 토큰

        // (나에게) 메시지 전송
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> response = restTemplate.exchange(SEND_ME_URL, HttpMethod.POST,
            requestEntity, Object.class);

        // 응답
        System.out.println("Response: " + response.getBody());
    }

    // 나에게 메시지 보내기 DOCS 에 나와 있는 데이터 형식
    private MultiValueMap<String, String> createTemplateObject(
        OrderRequestDTO orderRequestDTO) {
        TemplateObject templateObject = new TemplateObject(orderRequestDTO.message());
        String textTemplateJson;
        try {
            textTemplateJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "json 형식이 올바르지 않습니다.");
        }
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", textTemplateJson);
        return body;
    }

    private void removeFromWishList(Long userId, Long optionId) {
        Option option = optionRepository.findById(optionId).get();
        Product product = option.getProduct();
        cartItemRepository.findByUserIdAndProductId(userId, product.getId())
            .ifPresent(cartItemRepository::delete);
    }
}
