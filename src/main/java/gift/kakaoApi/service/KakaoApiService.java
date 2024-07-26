package gift.kakaoApi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.order.entity.Orders;
import gift.kakaoApi.dto.token.KakaoTokenFailResponse;
import gift.kakaoApi.dto.token.KakaoTokenResponse;
import gift.kakaoApi.dto.userInfo.KakaoUserInfoResponse;
import gift.kakaoApi.dto.message.Link;
import gift.kakaoApi.dto.message.MessageResponse;
import gift.kakaoApi.dto.message.TemplateObject;
import gift.kakaoApi.exceptiion.KakaoLoginException;
import gift.kakaoApi.properties.KakaoProperties;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {

    public static final String BEARER_PREPIX = "Bearer ";
    private static final String KAKAO_AUTHORIZATION_CODE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String KAKAO_TOEKN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_ACCOUNT_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String KAKAO_SEND_MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public KakaoApiService(KakaoProperties properties, ObjectMapper objectMapper,
        RestClient restClient) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.client = restClient;
    }

    public String getKakaoLoginUri() {
        return UriComponentsBuilder.newInstance()
            .path(KAKAO_AUTHORIZATION_CODE_URL)
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUrl())
            .toUriString();
    }

    public KakaoTokenResponse getKakaoToken(String code) {
        LinkedMultiValueMap<String, String> body = creatBody(code);

        return client.post()
            .uri(URI.create(KAKAO_TOEKN_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .exchange((request, response) -> {
                if (response.getStatusCode().is4xxClientError()) {
                    KakaoTokenFailResponse failResponse = objectMapper.readValue(response.getBody(),
                        KakaoTokenFailResponse.class);
                    throw new KakaoLoginException(failResponse.errorCode(),
                        failResponse.errorDescription());
                }

                return objectMapper.readValue(response.getBody(), KakaoTokenResponse.class);
            });
    }

    public KakaoUserInfoResponse getKakaoAccount(String accessToken) {
        return client.get()
            .uri(URI.create(KAKAO_ACCOUNT_URL))
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREPIX + accessToken)
            .retrieve()
            .body(KakaoUserInfoResponse.class);
    }

    public MessageResponse sendKakaoMessage(String accessToken, Orders order) {
        String orderMessage = createOrderMessage(order);
        Link link = new Link("http://localhost:8080", "http://localhost:8080");
        TemplateObject templateObject = new TemplateObject("text", orderMessage, link);

        return client.post()
            .uri(URI.create(KAKAO_SEND_MESSAGE_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREPIX + accessToken)
            .body(templateObject.toRequestBody())
            .retrieve()
            .body(MessageResponse.class);
    }

    public String createOrderMessage(Orders order) {
        String messageTemplate = "[상품 주문]\n"
            + "상품명: %s\n"
            + "옵션\n"
            + "  옵션명: %s\n"
            + "  수량: %d\n"
            + "요청사항: %s\n"
            + "상품 주문이 완료되었습니다.";

        String productName = order.getOption().getProduct().getName();
        String optionName = order.getOption().getName();
        int quantity = order.getQuantity();
        String requestMessage = order.getMessage();

        return String.format(messageTemplate, productName, optionName, quantity, requestMessage);
    }

    private LinkedMultiValueMap<String, String> creatBody(String code) {

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
