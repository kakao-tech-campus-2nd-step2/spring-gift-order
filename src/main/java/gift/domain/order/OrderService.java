package gift.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.cartItem.CartItem;
import gift.domain.cartItem.JpaCartItemRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionService;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.user.JpaUserRepository;
import gift.domain.user.User;
import gift.domain.user.dto.UserInfo;
import gift.domain.user.kakao.KaKaoProperties;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
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
    private final JpaProductRepository productRepository;
    private final JpaCartItemRepository cartItemRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KaKaoProperties kaKaoProperties;
    private final JpaUserRepository userRepository;

    @Autowired
    public OrderService(
        JpaOptionRepository jpaOptionRepository,
        OptionService optionService,
        JpaProductRepository jpaProductRepository,
        JpaCartItemRepository jpaCartItemRepository,
        RestTemplateBuilder restTemplateBuilder,
        ObjectMapper objectMapper,
        KaKaoProperties kaKaoProperties,
        JpaUserRepository userRepository
    ) {
        optionRepository = jpaOptionRepository;
        this.optionService = optionService;
        productRepository = jpaProductRepository;
        cartItemRepository = jpaCartItemRepository;
        restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.kaKaoProperties = kaKaoProperties;
        this.userRepository = userRepository;
    }

    /**
     * (나에게) 상품 선물하기
     */
    @Transactional
    public void order(OrderRequestDTO orderRequestDTO, UserInfo userInfo)
        throws JsonProcessingException, UnsupportedEncodingException {
        // 해당 상품의 옵션의 수량을 차감
        optionService.decreaseOptionQuantity(orderRequestDTO.optionId(), orderRequestDTO.quantity());

        // 해당 상품이 (나의) 위시리스트에 있는 경우 위시 리스트에서 삭제
        Option option = optionRepository.findById(orderRequestDTO.optionId()).get();
        Product product = option.getProduct();
        Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndProductId(userInfo.getId(),
            product.getId());
        if (cartItem.isPresent()) { cartItemRepository.delete(cartItem.get()); }

        // (나에게) 메세지 전송
        TemplateObject templateObject = new TemplateObject(orderRequestDTO.message());
        String textTemplateJson = objectMapper.writeValueAsString(templateObject);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", textTemplateJson);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        User user = userRepository.findById(userInfo.getId()).get();
        headers.setBearerAuth(user.getAccessToken()); // 엑세스 토큰

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> response = restTemplate.exchange(SEND_ME_URL, HttpMethod.POST,
            requestEntity, Object.class);

        // 응답
        System.out.println("Response: " + response.getBody());
    }
}
