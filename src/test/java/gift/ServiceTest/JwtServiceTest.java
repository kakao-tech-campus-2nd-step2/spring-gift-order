package gift.ServiceTest;

import gift.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testCreateJWT() {
        String id = "testId";
        String jwt = jwtService.createJWT(id);

        assertThat(jwt).isNotNull();
        assertThat(jwt).startsWith("eyJ");
    }

    @Test
    void testGetJWT() {
        String token = "Bearer testToken";
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        String jwt = jwtService.getJWT();

        assertThat(jwt).isEqualTo("testToken");
    }

    @Test
    void testGetMemberId() {
        String id = "testId";
        String jwt = jwtService.createJWT(id);

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        String memberId = jwtService.getMemberId();

        assertThat(memberId).isEqualTo(id);
    }

    @Test
    void testGetMemberIdWithInvalidToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThatThrownBy(() -> jwtService.getMemberId())
                .isInstanceOf(Exception.class);
    }

    @Test
    void testGetMemberIdWithNullToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThatThrownBy(() -> jwtService.getMemberId())
                .isInstanceOf(JwtException.class)
                .hasMessageContaining("토큰이 유효하지 않습니다.");
    }

    @Test
    void testGetMemberIdWithEmptyToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer ");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThatThrownBy(() -> jwtService.getMemberId())
                .isInstanceOf(JwtException.class)
                .hasMessageContaining("토큰이 유효하지 않습니다.");
    }
}
