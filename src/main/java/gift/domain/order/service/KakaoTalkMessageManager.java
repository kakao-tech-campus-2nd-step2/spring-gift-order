package gift.domain.order.service;

import gift.auth.AuthProvider;
import gift.auth.oauth.entity.OauthToken;
import gift.auth.oauth.service.OauthTokenManager;
import gift.domain.order.dto.OrderResponse;
import gift.domain.user.entity.User;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.FeedObjectRequest;
import gift.external.api.kakao.dto.FeedObjectRequest.Button;
import gift.external.api.kakao.dto.FeedObjectRequest.Content;
import gift.external.api.kakao.dto.FeedObjectRequest.Link;
import org.springframework.stereotype.Component;

@Component
public class KakaoTalkMessageManager {

    private final KakaoApiProvider kakaoApiProvider;
    private final OauthTokenManager oauthTokenManager;

    public KakaoTalkMessageManager(KakaoApiProvider kakaoApiProvider,
        OauthTokenManager oauthTokenManager) {
        this.kakaoApiProvider = kakaoApiProvider;
        this.oauthTokenManager = oauthTokenManager;
    }

    public Integer sendMessageToMe(User user, OrderResponse orderResponse) {
        FeedObjectRequest templateObject = new FeedObjectRequest(
            "feed",
            new Content(
                "주문해 주셔서 감사합니다.",
                orderResponse.productResponse().imageUrl(),
                orderResponse.message(),
                new Link("http://localhost:8080/api/products/" + orderResponse.productResponse().id())
            ),
            new Button[]{
                new Button(
                    "자세히 보기",
                    new Link("http://localhost:8080/api/products/" + orderResponse.productResponse().id())
                )
            }
        );
        OauthToken oauthToken = oauthTokenManager.findByUserAndProvider(user, AuthProvider.KAKAO);
        OauthToken renewedToken = oauthTokenManager.renew(oauthToken);

        return kakaoApiProvider.sendMessageToMe(renewedToken.getAccessToken(), templateObject);
    }
}
