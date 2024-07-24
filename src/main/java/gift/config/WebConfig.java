package gift.config;

import gift.controller.auth.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/members/oauth/**")
                .excludePathPatterns("/api/members/login")
                .excludePathPatterns("/api/members/register")
                .excludePathPatterns("/api/kakao/get-oauth")
                .excludePathPatterns("/api/kakao/get-token");
    }
}
