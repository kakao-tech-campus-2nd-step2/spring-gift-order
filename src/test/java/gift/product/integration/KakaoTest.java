package gift.product.integration;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.controller.KakaoController;
import gift.product.service.KakaoProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoTest {

    private final KakaoController kakaoController;

    @Autowired
    private KakaoProperties properties;

    @Autowired
    public KakaoTest(KakaoController kakaoController) {
        this.kakaoController = kakaoController;
    }

    @Test
    void testProperties() {
        assertThat(properties.clientId()).isNotEmpty();
        assertThat(properties.redirectUrl()).isEqualTo("http://localhost:8080");
    }

    @Test
    void testAuthRequest() {
        assertThat(kakaoController.login()).isEqualTo(
            "redirect:https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
                + "&redirect_uri=" + properties.redirectUrl()
                + "&client_id=" + properties.clientId()
        );
    }
}
