package gift.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gift.interceptor.JwtInterceptor;

/** WebConfig 클래스는 Spring MVC 설정을 커스터마이징.
 * 이 클래스는 JWT 토큰을 검증하는 JwtInterceptor를 특정 경로에 적용하고,
 * 로그인 및 회원가입 경로를 예외 처리하여 인증이 필요한 경로에 대한 접근을 제어하는 역할 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/wish/**") // Wish 관련 경로에 인터셉터 적용
                .excludePathPatterns("/login", "/register"); // 로그인, 회원가입 등의 경로는 제외할 수 있다.
    }
}