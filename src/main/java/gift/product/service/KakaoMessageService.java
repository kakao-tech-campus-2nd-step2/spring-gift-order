package gift.product.service;

import gift.config.KakaoMessageConfig;
import gift.product.service.command.BuyProductMessageCommand;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class KakaoMessageService {
    private final KakaoMessageConfig kakaoMessageConfig;

    public KakaoMessageService(KakaoMessageConfig kakaoMessageConfig) {
        this.kakaoMessageConfig = kakaoMessageConfig;
    }

    public boolean sendBuyProductMessage(BuyProductMessageCommand buyProductMessageCommand) {
        var client = kakaoMessageConfig.createMessageSendClient(buyProductMessageCommand.accessToken());
        var body = new LinkedMultiValueMap<>();

        var response = client
                .post()
                .body((buyProductMessageCommand.toKakaoMessageTemplate()))
                .retrieve()
                .toEntity(String.class);

        return true;
    }
}
