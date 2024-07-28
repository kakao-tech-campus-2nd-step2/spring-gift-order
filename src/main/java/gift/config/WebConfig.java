package gift.config;

import gift.interceptor.KakaoAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final KakaoAuthInterceptor kakaoAuthInterceptor;

    @Autowired
    public WebConfig(KakaoAuthInterceptor kakaoAuthInterceptor) {
        this.kakaoAuthInterceptor = kakaoAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(kakaoAuthInterceptor)
                .addPathPatterns("/api/**", "/members/**")
                .excludePathPatterns("/members/login", "/members/oauth/kakao");
    }
}
