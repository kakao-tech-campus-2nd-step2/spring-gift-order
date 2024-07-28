package gift.interceptor;

import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.KakaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Component
public class KakaoAuthInterceptor implements HandlerInterceptor {

    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;

    @Autowired
    public KakaoAuthInterceptor(KakaoUtil kakaoUtil, MemberRepository memberRepository) {
        this.kakaoUtil = kakaoUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Map<String, Object> userInfo = kakaoUtil.getUserInfo(token);
                request.setAttribute("kakaoUserInfo", userInfo);

                Long memberId = Long.parseLong(userInfo.get("id").toString());
                Optional<Member> optionalMember = memberRepository.findById(memberId);

                if (optionalMember.isPresent()) {
                    Member member = optionalMember.get();
                    request.setAttribute("role", member.getRole());
                    return true;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
