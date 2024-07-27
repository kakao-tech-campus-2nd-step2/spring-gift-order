package gift.user.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.order.dto.KakaoMessageRequestBody;
import gift.user.dto.response.KakaoTokenResponse;
import gift.user.dto.response.KakaoUserInfoResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLoginClient {

    private final RestClient restClient;

    private final KakaoProperties properties;
    private final ObjectMapper objectMapper;

    public KakaoLoginClient(RestClient restClient, KakaoProperties properties,
        ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public KakaoTokenResponse getKakaoTokenResponse(String code) {
        var url = properties.tokenUrl();
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", code);

        return restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new CustomException(ErrorCode.KAKAO_LOGIN_ERROR);
            })
            .body(KakaoTokenResponse.class);
    }

    public KakaoUserInfoResponse getKakaoUserId(String token) {
        var url = properties.userInfoUrl() + "?property_keys=[]";
        return restClient.get()
            .uri(url)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new CustomException(ErrorCode.KAKAO_LOGIN_ERROR);
            })
            .body(KakaoUserInfoResponse.class);
    }

    public void sendMessage(String token, KakaoMessageRequestBody requestBody)
        throws JsonProcessingException {
        var url = properties.messageUrl();
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_id", requestBody.templateId());
        body.add("template_args", objectMapper.writeValueAsString(requestBody.templateArgs()));
        restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + token)
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new CustomException(ErrorCode.KAKAO_LOGIN_ERROR);
            })
            .body(String.class);
    }

}
