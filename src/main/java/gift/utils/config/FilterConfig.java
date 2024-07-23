package gift.utils.config;

import gift.repository.TokenRepository;
import gift.utils.JwtTokenProvider;
import gift.utils.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public FilterConfig(JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
    }


    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtTokenProvider, tokenRepository));
        registrationBean.addUrlPatterns("/*"); // 모든 경로에 필터 적용
        return registrationBean;
    }
}
