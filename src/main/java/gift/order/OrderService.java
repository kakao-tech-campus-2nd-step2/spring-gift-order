package gift.order;

import static gift.exception.ErrorMessage.KAKAO_AUTHENTICATION_FAILED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.exception.InvalidAccessTokenException;
import gift.member.MemberService;
import gift.oauth.KakaoOauthConfigure;
import gift.option.OptionService;
import gift.option.entity.Option;
import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import gift.order.dto.KakaoUserInfoDTO;
import gift.wishlist.WishlistService;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
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

    @Transactional
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
}
