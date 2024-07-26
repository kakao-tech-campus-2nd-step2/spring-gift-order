package gift.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.oauth.response.KakaoInfoResponse;
import gift.oauth.response.KakaoTokenResponse;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoApiService {

    private final KakaoApiSecurityProperties kakaoApiSecurityProps;
    private final RestClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoApiService(KakaoApiSecurityProperties kakaoApiSecurityProps,
        RestClient restClient) {
        this.kakaoApiSecurityProps = kakaoApiSecurityProps;
        this.client = restClient;
    }

    public URI getKakaoLoginPage() {
        return kakaoApiSecurityProps.getLoginUri();
    }

    public KakaoTokenResponse requestToken(String code) {
        var uri = kakaoApiSecurityProps.getTokenUri();
        var body = getTokenRequestBody(code);
        var response = client.post().uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body)
            .retrieve()
            .toEntity(KakaoTokenResponse.class);
        return response.getBody();
    }

    public Long getKakaoId(String token) {
        var uri = kakaoApiSecurityProps.getUserInfoUri();
        var response = client.post().uri(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
            .retrieve().toEntity(KakaoInfoResponse.class);
        return response.getBody().id();
    }

    public void sendMessageToMe(String token, String text)
        throws JsonProcessingException {
        var uri = kakaoApiSecurityProps.getMemoSend();
        var body = getSelfMessageRequestBody(text);
        client.post().uri(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body).contentType(MediaType.APPLICATION_JSON)
            .headers(httpHeaders -> httpHeaders.setBearerAuth(token)).retrieve().toBodilessEntity();
    }

    public LinkedMultiValueMap<String, String> getTokenRequestBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoApiSecurityProps.getClientId());
        body.add("redirect_uri", kakaoApiSecurityProps.getRedirectUri());
        body.add("code", code);
        return body;
    }

    public MultiValueMap<String, String> getSelfMessageRequestBody(String text)
        throws JsonProcessingException {
        Map<String, String> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", text);
        templateObject.put("link", null);
        templateObject.put("button_title", "버튼");
        var jsonTemplateObject = objectMapper.writeValueAsString(templateObject);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonTemplateObject);
        return body;
    }


}
