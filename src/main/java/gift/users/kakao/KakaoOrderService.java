package gift.users.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gift.administrator.option.OptionDTO;
import gift.administrator.option.OptionService;
import gift.administrator.product.ProductService;
import gift.error.KakaoOrderException;
import gift.users.wishlist.WishListService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class KakaoOrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TokenService tokenService;
    private final ProductService productService;
    private final OptionService optionService;
    private final RestClient.Builder restClientBuilder;
    private final KakaoProperties kakaoProperties;
    private final WishListService wishListService;

    private static final String HEADER_NAME = "Authorization";

    public KakaoOrderService(TokenService tokenService, ProductService productService,
        OptionService optionService, RestClient.Builder restClientBuilder,
        KakaoProperties kakaoProperties, WishListService wishListService) {
        this.tokenService = tokenService;
        this.productService = productService;
        this.optionService = optionService;
        this.restClientBuilder = restClientBuilder;
        this.kakaoProperties = kakaoProperties;
        this.wishListService = wishListService;
    }

    public KakaoOrderDTO kakaoOrder(long userId, KakaoOrderDTO kakaoOrderDTO,
        String orderDateTime) {

        if(productService.existsByProductId(kakaoOrderDTO.productId())){
            throw new KakaoOrderException("없는 상품입니다.");
        }

        OptionDTO optionDTO = optionService.findOptionById(kakaoOrderDTO.optionId());
        if (optionDTO.getProductId() != kakaoOrderDTO.productId()) {
            throw new KakaoOrderException(
                kakaoOrderDTO.productId() + " 상품에 " + kakaoOrderDTO.optionId() + " 옵션이 존재하지 않습니다.");
        }

        optionService.subtractOptionQuantityErrorIfNotPossible(kakaoOrderDTO.optionId(),
            kakaoOrderDTO.quantity());

        String accessToken = tokenService.findToken(userId, "kakao");
        String messageObject = makeKakaoMessage(kakaoOrderDTO);

        sendKakaoMessage(accessToken, messageObject);

        optionService.subtractOptionQuantity(kakaoOrderDTO.optionId(), kakaoOrderDTO.quantity());

        wishListService.findOrderAndDeleteIfExists(userId, kakaoOrderDTO.productId(),
            kakaoOrderDTO.optionId());
        return new KakaoOrderDTO(kakaoOrderDTO.productId(), kakaoOrderDTO.optionId(),
            kakaoOrderDTO.quantity(), orderDateTime, kakaoOrderDTO.message());
    }

    private String makeKakaoMessage(KakaoOrderDTO kakaoOrderDTO) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode templateObject = objectMapper.createObjectNode();
        templateObject.put("object_type", "text");
        templateObject.put("text",
            "주문내역\n상품: " + productService.getProductById(kakaoOrderDTO.productId()).getName()
                + "\n옵션: " + optionService.findOptionById(kakaoOrderDTO.optionId()).getName()
                + "\n수량: " + kakaoOrderDTO.quantity() + "\n메시지: " + kakaoOrderDTO.message());

        ObjectNode linkObject = objectMapper.createObjectNode();
        linkObject.put("web_url", "");
        linkObject.put("mobile_web_url", "");
        templateObject.set("link", linkObject);

        String messageObject;
        try {
            messageObject = objectMapper.writeValueAsString(templateObject);
            logger.info("json: {}", messageObject);
        } catch (JsonProcessingException e) {
            throw new KakaoOrderException("잘못된 json 형식입니다.");
        }
        return URLEncoder.encode(messageObject, StandardCharsets.UTF_8);
    }

    private void sendKakaoMessage(String accessToken, String messageObject) {
        ResponseEntity<String> response;
        try {
            response = restClientBuilder.build().post().uri(kakaoProperties.sendToMeUrl())
                .header(HEADER_NAME, kakaoProperties.userHeaderValue() + " " + accessToken)
                .body("template_object=" + messageObject).retrieve().toEntity(String.class);

        } catch (RestClientException e) {
            throw new KakaoOrderException("카카오 메시지를 보내는 데에 실패했습니다." + e);
        }

        if (response.getBody() == null || !response.getBody().contains("0")) {
            throw new KakaoOrderException("카카오 메시지를 보내는 데에 실패했습니다.");
        }
    }
}
