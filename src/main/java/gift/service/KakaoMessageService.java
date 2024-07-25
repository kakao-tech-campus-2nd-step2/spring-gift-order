package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.KakaoMessageTemplate;
import gift.dto.response.OrderResponse;
import gift.repository.KakaoAccessTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

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
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoMessageTemplate template = new KakaoMessageTemplate(order.message());
        String ss = null;
        try {
            ss = objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            System.out.println("heelo");
        }

        multiValueMap.add("template_object", ss);
        System.out.println(ss);

        String accessToken = accessTokenRepository.getAccessToken(memberId);

        String body = restClient.post()
                .uri(MESSAGE_URI)
                .body(multiValueMap)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .retrieve()
                .body(String.class);
        System.out.println(body);
    }
}
