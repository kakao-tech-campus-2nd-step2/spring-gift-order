package gift.interceptor;

import gift.service.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;

    public AuthInterceptor(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        if (!jwtTokenService.isValidateToken(token)) {
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        request.setAttribute("memberId", jwtTokenService.getMemberId(token));
        return true;
    }
}
