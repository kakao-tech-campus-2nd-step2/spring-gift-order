package gift.product.service;

import gift.config.KakaoMessageConfig;
import gift.product.exception.kakao.KakaoMessageException;
import gift.product.service.command.BuyProductMessageCommand;
import org.springframework.stereotype.Service;

@Service
public class KakaoMessageService {
    private final KakaoMessageConfig kakaoMessageConfig;

    public KakaoMessageService(KakaoMessageConfig kakaoMessageConfig) {
        this.kakaoMessageConfig = kakaoMessageConfig;
    }

    public void sendBuyProductMessage(BuyProductMessageCommand buyProductMessageCommand) {
        var client = kakaoMessageConfig.createMessageSendClient(buyProductMessageCommand.accessToken());
        var body = buyProductMessageCommand.toKakaoMessageTemplate();

        var response = client
                .post()
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (response.getStatusCode().isError()) {
            throw new KakaoMessageException();
        }
    }
}
