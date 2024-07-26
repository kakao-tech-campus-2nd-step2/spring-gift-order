package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoToken;
import gift.domain.Option;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.LoginMember;
import gift.dto.TemplateObject;
import gift.dto.request.OrderRequest;
import gift.exception.CustomException;
import gift.repository.KakaoTokenRepository;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;

import static gift.exception.ErrorCode.DATA_NOT_FOUND;
import static gift.exception.ErrorCode.SEND_MSG_FAILED_ERROR;

@Service
public class OrderService {

    @Value("${kakao.send-message.url}")
    private String sendMessageUrl;

    @Value("${service.home.web_url}")
    private String homeUrl;

    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoTokenRepository kakaoTokenRepository;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository, KakaoTokenRepository kakaoTokenRepository) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public void order(LoginMember member, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId()).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        option.subtract(orderRequest.quantity());

        deleteFromWishList(member.getId(), option.getProduct());

        KakaoToken token = kakaoTokenRepository.findKakaoTokensByMember_Id(member.getId());
        sendMessage(orderRequest.message(), token.getAccessToken());
      }

    private void deleteFromWishList(Long memberId, Product product) {
        Optional<Wish> wish = wishRepository.findWishByMember_IdAndProduct(memberId, product);
        wish.ifPresent(wishRepository::delete);
    }

    private void sendMessage(String message, String accessToken) {
        RestClient client = RestClient.builder().build();
        LinkedMultiValueMap<String, String> body = createSendMsgBody(message);

        ResponseEntity<String> response = client.post()
                .uri(URI.create(sendMessageUrl))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(SEND_MSG_FAILED_ERROR);
        }
    }

    private LinkedMultiValueMap<String, String> createSendMsgBody(String message) {
        LinkedMultiValueMap<String, String> templateObject = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        TemplateObject template = new TemplateObject("text", message, new TemplateObject.Link(homeUrl));

        try {
            String jsonTemplateObject = objectMapper.writeValueAsString(template);
            templateObject.add("template_object", jsonTemplateObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return templateObject;
    }

}
