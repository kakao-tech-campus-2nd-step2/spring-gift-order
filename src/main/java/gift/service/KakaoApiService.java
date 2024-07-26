package gift.service;

import gift.client.KakaoApiClient;
import gift.client.requestBody.KakaoMessageRequestBodyGenerator;
import gift.client.requestBody.KakaoTokenRequestBodyGenerator;
import gift.dto.request.OrderRequest;
import gift.dto.response.KakaoTokenResponse;
import gift.repository.KakaoAccessTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoTokenRequestBodyGenerator kakaoTokenRequestBodyGenerator;
    private final KakaoMessageRequestBodyGenerator kakaoMessageRequestBodyGenerator;
    private final KakaoAccessTokenRepository kakaoAccessTokenRepository;

    public KakaoApiService(KakaoApiClient kakaoApiClient, KakaoTokenRequestBodyGenerator kakaoTokenRequestBodyGenerator, KakaoMessageRequestBodyGenerator kakaoMessageRequestBodyGenerator, KakaoAccessTokenRepository kakaoAccessTokenRepository) {
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoTokenRequestBodyGenerator = kakaoTokenRequestBodyGenerator;
        this.kakaoMessageRequestBodyGenerator = kakaoMessageRequestBodyGenerator;
        this.kakaoAccessTokenRepository = kakaoAccessTokenRepository;
    }

    public KakaoTokenResponse getKakaoToken(String code) {
        kakaoTokenRequestBodyGenerator.setCode(code);

        return kakaoApiClient.getKakaoToken(kakaoTokenRequestBodyGenerator.toMultiValueMap());
    }

    public String getMemberEmail(String token) {
        return kakaoApiClient.getMemberEmail(token);
    }

    public void sendMessageToMe(Long memberId, OrderRequest orderRequest) {
        String accessToken = kakaoAccessTokenRepository.getAccessToken(memberId);

        kakaoMessageRequestBodyGenerator.setMessage(orderRequest.message());

        kakaoApiClient.sendMessageToMe(accessToken, kakaoMessageRequestBodyGenerator.toMultiValueMap());
    }
}
