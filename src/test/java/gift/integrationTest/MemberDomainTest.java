package gift.integrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.controller.apiResponse.MemberLoginApiResponse;
import gift.domain.controller.apiResponse.MemberRegisterApiResponse;
import gift.domain.dto.request.member.LocalMemberRequest;
import gift.domain.dto.request.member.MemberRequest;
import gift.domain.repository.MemberRepository;
import gift.utilForTest.TestUtil;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.transaction.annotation.Transactional;

//TODO: 인텔리제이 환경변수가 테스트시에도 적용되도록 수정필요
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
class MemberDomainTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("[ApiIntegrationTest] 회원가입")
    void registerMember() {
        //given
        LocalMemberRequest request = new LocalMemberRequest("testaccount@example.com", "test");

        //when
        var actualResponse = restTemplate.exchange(
            new RequestEntity<>(request, new HttpHeaders(), HttpMethod.POST, testUtil.getUri(port, "/api/members/register")),
            MemberRegisterApiResponse.class);

        //then
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(actualResponse.getBody()).getToken()).isNotNull();
    }

    @Test
    @DisplayName("[ApiIntegrationTest] 로그인")
    void loginMember() {
        //given
        LocalMemberRequest request = new LocalMemberRequest("test2@example.com", "test");
        restTemplate.exchange(
            new RequestEntity<>(request, new HttpHeaders(), HttpMethod.POST, testUtil.getUri(port, "/api/members/register")),
            MemberRegisterApiResponse.class);

        //when
        var actualResponse = restTemplate.exchange(
            new RequestEntity<>(request, new HttpHeaders(), HttpMethod.POST, testUtil.getUri(port, "/api/members/login")),
            MemberLoginApiResponse.class);

        //then
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(actualResponse.getBody()).getToken()).isNotNull();
    }
}