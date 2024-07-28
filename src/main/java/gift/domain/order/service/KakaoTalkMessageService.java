package gift.domain.order.service;

import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.service.OauthTokenService;
import gift.domain.order.dto.OrderResponse;
import gift.domain.user.entity.User;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.FeedObjectRequest;
import gift.external.api.kakao.dto.FeedObjectRequest.Button;
import gift.external.api.kakao.dto.FeedObjectRequest.Content;
import gift.external.api.kakao.dto.FeedObjectRequest.Link;
import org.springframework.stereotype.Component;

@Component
public class KakaoTalkMessageService {

    private final KakaoApiProvider kakaoApiProvider;
    private final OauthTokenService oauthTokenService;

    public KakaoTalkMessageService(
        KakaoApiProvider kakaoApiProvider,
        OauthTokenService oauthTokenService
    ) {
        this.kakaoApiProvider = kakaoApiProvider;
        this.oauthTokenService = oauthTokenService;
    }

    public Integer sendMessageToMe(User user, OrderResponse orderResponse) {
        String tempLinkUrl = "http://localhost:8080/api/products/" + orderResponse.orderItems().get(0).product().id();
        FeedObjectRequest templateObject = new FeedObjectRequest(
            "feed",
            new Content(
                "주문해 주셔서 감사합니다.",
                orderResponse.orderItems().get(0).product().imageUrl(),
                orderResponse.recipientMessage(),
                new Link(tempLinkUrl)
            ),
            new Button[]{
                new Button(
                    "자세히 보기",
                    new Link(tempLinkUrl)
                )
            }
        );

        OauthToken oauthToken = oauthTokenService.getOauthToken(user, AuthProvider.KAKAO);

        return kakaoApiProvider.sendMessageToMe(oauthToken.getAccessToken(), templateObject);
    }
}
