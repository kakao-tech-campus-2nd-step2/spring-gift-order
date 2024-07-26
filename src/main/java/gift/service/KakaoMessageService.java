package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.KakaoMessageTemplate;
import gift.dto.response.OrderResponse;
import gift.exception.KakaoApiHasProblemException;
import gift.repository.KakaoAccessTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class KakaoMessageService {

    private static final String MESSAGE_URI = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final KakaoAccessTokenRepository accessTokenRepository;
    private final RestClient restClient;

    public KakaoMessageService(KakaoAccessTokenRepository accessTokenRepository, RestClient.Builder builder) {
        this.accessTokenRepository = accessTokenRepository;
        this.restClient = builder.build();
    }

    public void sendToMe(Long memberId, OrderResponse order) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        KakaoMessageTemplate template = new KakaoMessageTemplate(order.message());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            multiValueMap.add("template_object", objectMapper.writeValueAsString(template));
        } catch (JsonProcessingException e) {
            Logger.getLogger(KakaoMessageService.class.getName()).log(Level.WARNING, "ObjectMapper가 JSON 파싱에 실패");
        }

        String accessToken = accessTokenRepository.getAccessToken(memberId);

        int maxRetries = 4;
        int retryCount = 0;
        List<Exception> exceptions = new ArrayList<>();
        while (retryCount < maxRetries) {
            try {
                restClient.post()
                        .uri(MESSAGE_URI)
                        .body(multiValueMap)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                        .retrieve();
            } catch (Exception e) {
                exceptions.add(e);
                retryCount++;
            }
        }
        throw new KakaoApiHasProblemException("카카오API의 '나에게 메세지 보내기' 기능에 문제가 생겼습니다.", exceptions);
    }
}
