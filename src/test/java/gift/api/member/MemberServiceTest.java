package gift.api.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void createBody() {
        // given
        var grantType = "authorization_code";
        var clientId = "c488398db1a3700c7de43d25292367ca";
        var redirectUrl = "http://localhost:8080";

        // when
        var body = memberService.createBody("code");

        // then
        assertAll(
            () -> assertThat(body.getFirst("grant_type")).isEqualTo(grantType),
            () -> assertThat(body.getFirst("client_id")).isEqualTo(clientId),
            () -> assertThat(body.getFirst("redirect_url")).isEqualTo(redirectUrl)
        );
    }
}