package gift.ControllerTest;

import gift.controller.MemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class MemberControllerTest {
    @Autowired
    MemberController memberController;

    @Test
    @DisplayName("카테고리 FindAll 테스트")
    void testFindAllCategories() {
        ResponseEntity<String> responseEntity = memberController.loginByKakao();
        HttpHeaders headers = responseEntity.getHeaders();

        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

        assertThat(authorizationHeader).isNotNull();
        System.out.println("Authorization Header: " + authorizationHeader);
    }

}
