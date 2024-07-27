package gift.order;

import static gift.exception.ErrorMessage.KAKAO_AUTHENTICATION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.exception.InvalidAccessTokenException;
import gift.login.KakaoOauthConfigure;
import gift.member.MemberService;
import gift.option.OptionService;
import gift.option.entity.Option;
import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import gift.order.dto.DefaultMessageTemplate;
import gift.order.dto.KakaoUserInfoDTO;
import gift.wishlist.WishlistService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@Transactional
public class OrderService {

    private final RestClient restClient;
    private final KakaoOauthConfigure kakaoOauthConfigure;
    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final MemberService memberService;

    public OrderService(
        RestClient restClient,
        KakaoOauthConfigure kakaoOauthConfigure,
        OptionService optionService,
        MemberService memberService,
        WishlistService wishlistService
    ) {
        this.restClient = restClient;
        this.kakaoOauthConfigure = kakaoOauthConfigure;
        this.optionService = optionService;
        this.memberService = memberService;
        this.wishlistService = wishlistService;
    }

    public CreateOrderResponseDTO createOrder(
        CreateOrderRequestDTO createOrderRequestDTO,
        String accessToken
    ) {
        Option option = optionService.subtract(
            createOrderRequestDTO.getOptionId(),
            createOrderRequestDTO.getQuantity()
        );

        wishlistService.deleteWishlistIfExists(
            option.getProduct(),
            memberService.getMember(getEmailFromAccessToken(accessToken))
        );

        sendMessage(createOrderRequestDTO, accessToken);

        return new CreateOrderResponseDTO(
            option.getProduct().getId(),
            createOrderRequestDTO.getOptionId(),
            createOrderRequestDTO.getQuantity(),
            LocalDateTime.now(),
            createOrderRequestDTO.getMessage()
        );
    }

    private String getEmailFromAccessToken(String accessToken) {
        return restClient.get()
            .uri(kakaoOauthConfigure.getUserInfoFromAccessTokenURL())
            .header("Authorization", accessToken)
            .accept(APPLICATION_FORM_URLENCODED)
            .exchange((request, response) -> {
                if (response.getStatusCode().is2xxSuccessful()) {
                    return Objects.requireNonNull(response.bodyTo(KakaoUserInfoDTO.class))
                        .getKakaoAccount()
                        .getEmail();
                }
                throw new InvalidAccessTokenException(KAKAO_AUTHENTICATION_FAILED);
            });
    }

    private void sendMessage(CreateOrderRequestDTO createOrderRequestDTO, String accessToken) {
        restClient.post()
            .uri(kakaoOauthConfigure.getMessageSendURL())
            .header("Authorization", accessToken)
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(generateMessageBody(createOrderRequestDTO))
            .exchange((request, response) -> {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new InvalidAccessTokenException(KAKAO_AUTHENTICATION_FAILED);
                }
                return ResponseEntity.ok();
            });
    }

    private LinkedMultiValueMap<String, String> generateMessageBody(
        CreateOrderRequestDTO createOrderRequestDTO
    ) {
        DefaultMessageTemplate defaultMessageTemplate = new DefaultMessageTemplate(
            "text", createOrderRequestDTO.getMessage(), Map.of(), "버튼"
        );

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", defaultMessageTemplate.toJson());

        return body;
    }
}
