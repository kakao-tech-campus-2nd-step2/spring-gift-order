package gift.domain.order.service;

import gift.auth.AuthProvider;
import gift.auth.oauth.entity.OauthToken;
import gift.auth.oauth.service.OauthTokenService;
import gift.domain.order.dto.OrderResponse;
import gift.external.api.kakao.dto.FeedObjectRequest;
import gift.domain.user.entity.User;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.FeedObjectRequest.Button;
import gift.external.api.kakao.dto.FeedObjectRequest.Content;
import gift.external.api.kakao.dto.FeedObjectRequest.Link;
import org.springframework.stereotype.Service;

@Service
public class KakaoTalkMessageService {

    private final KakaoApiProvider kakaoApiProvider;
    private final OauthTokenService oauthTokenService;

    public KakaoTalkMessageService(KakaoApiProvider kakaoApiProvider,
        OauthTokenService oauthTokenService) {
        this.kakaoApiProvider = kakaoApiProvider;
        this.oauthTokenService = oauthTokenService;
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
        OauthToken oauthToken = oauthTokenService.findByUserAndProvider(user, AuthProvider.KAKAO);
        OauthToken renewedToken = oauthTokenService.renew(oauthToken);

        return kakaoApiProvider.sendMessageToMe(renewedToken.getAccessToken(), templateObject);
    }
}
