package gift.config;

import gift.filter.AdminFilter;
import gift.filter.JwtFilter;
import gift.repository.UserRepository;
import gift.util.UserUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${spring.var.token-prefix}")
    private String tokenPrefix;

    private final UserUtility userUtility;
    private final UserRepository userRepository;

    public FilterConfig(UserUtility userUtility, UserRepository userRepository) {
        this.userUtility = userUtility;
        this.userRepository = userRepository;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(tokenPrefix, userUtility));
        registrationBean.addUrlPatterns("/api/products/*");
        registrationBean.addUrlPatterns("/api/orders/*");
        registrationBean.addUrlPatterns("/api/wishlists/*");
        registrationBean.addUrlPatterns("/api/users/me");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilter() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminFilter(tokenPrefix, userUtility));
        registrationBean.addUrlPatterns("/admin/*");
        registrationBean.addUrlPatterns("/api/categories/*");
        return registrationBean;
    }
}
