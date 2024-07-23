package gift.interceptor;

import gift.service.JwtUtil;
import gift.service.KakaoMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final KakaoMemberService kakaoMemberService;

    public TokenInterceptor(JwtUtil jwtUtil, KakaoMemberService kakaoMemberService) {
        this.jwtUtil = jwtUtil;
        this.kakaoMemberService = kakaoMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (kakaoMemberService.checkTokenExists(token) || jwtUtil.isTokenValid(token)) {
                return true;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
