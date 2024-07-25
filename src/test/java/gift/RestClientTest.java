package gift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.exception.AuthenticationException;
import gift.common.properties.KakaoProperties;
import gift.service.dto.KakaoTokenDto;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

record Template(
        String object_type,
        Content content,
        ItemContent item_content
) {}

record Content(
        String title,
        String description,
        Link[] link
){}

record ItemContent(
        String profile_text,
        String title_image_text,
        String title_image_category,
        Item[] items,
        String sum,
        String sum_op
){}
record Item(
        String item,
        String item_op){}

record Link() {}

@ActiveProfiles("test")
@SpringBootTest
public class RestClientTest {
    private final RestClient client = RestClient.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KakaoProperties properties;

    @MockBean
    private RedissonClient redissonClient;

    @Test
    void test0() {
        System.out.println(properties);
    }

    @Test
    void getKakaoAccessToken() {
        var url = properties.tokenUrl();
        var code = "sJD6Gv6NGW0x6R69ff3_AxIy-gVjuI0-yz8I5M8DxXNd4fG3r4gGbgAAAAQKPCPnAAABkOUv0jjSDh85zpcCzQ";
        final var body = createBody(code);
        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)     // request body
                .exchange((req, res) -> {
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
            if (res.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                return objectMapper.readValue(res.getBody(), KakaoTokenDto.class);
            }
            return "";
        });
        System.out.println(response);
    }

    @Test
    void getKakaoMemberInfo() {
        var access_token = "EXzy9fHlc7wjc6GlfR1uRVo9yKYPTqJHAAAAAQorDR8AAAGQ3w6vZyn2EFsnJsRZ?";
        var response = client.post()
                .uri(URI.create(properties.memberInfoUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + access_token)
                .retrieve()
                .toEntity(String.class);

        System.out.println(response);
    }

    @Test
    void refreshAccessToken() {
        var refreshToken = "AjDT-6Ig_9NF361BAz4n2CFJTO-h7OO4AAAAAgo9cxgAAAGQ5PWPECn2EFsnJsRZ";
        var response = client.post()
                .uri(URI.create(properties.refreshUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(createBodyForRefreshAccessToken(refreshToken))
                .exchange((req, res) -> {
                    if (res.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                        return objectMapper.readValue(res.getBody(), KakaoTokenDto.class);
                    }
                    throw new AuthenticationException("kakao token refresh failed");
                });

        System.out.println(response);
    }

    @Test
    void signOutKakao() {
        String accessToken = "kqVhTXL0jC2IZaWxvlqYCEXAFososlVYAAAAAQo9c00AAAGQ5PdLGCn2EFsnJsRZ";
        var res = client.post()
                .uri(URI.create(properties.logoutUrl()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new AuthenticationException("Logout failed");
                });
        System.out.println(res);
    }

    @Test
    void sendSelfMessage() throws JsonProcessingException {
        String accessToken = "I58AJ4Yu3IOtwfmuCskpu820VLLJQVGqAAAAAQo9cpcAAAGQ5TDRVSn2EFsnJsRZ";
        Item item = new Item("상품1", "1000원");
        ItemContent itemContent = new ItemContent(
                "선물하기", "상품명1", "옵션1",
                new Item[]{item}, "Total",
                "1000원");
        String description = "여기에 메세지가 표시될듯?";
        Content content = new Content("상품을 주문했습니다.",  description, null);
        Template template = new Template("feed", content, itemContent);

        String template_str = objectMapper.writeValueAsString(template);
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        map.set("template_object", template_str);

        var res = client.post()
                .uri(properties.selfMessageUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(map)
                .retrieve()
                .toEntity(String.class);

        System.out.println(res);
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", code);
        return body;
    }

    private @NotNull LinkedMultiValueMap<String, String> createBodyForRefreshAccessToken(String refreshToken) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", properties.clientId());
        body.add("refresh_token", refreshToken);
        return body;
    }
}
