package gift.filter;

import gift.domain.AuthToken;
import gift.repository.token.TokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class OAuthTokenFilter implements Filter {

    private final TokenRepository tokenRepository;
    public OAuthTokenFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        if (path.equals("/home") || path.equals("/oauth/renew/kakao") || path.startsWith("/members") || path.startsWith("/login/oauth") || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthToken authToken = (AuthToken) httpRequest.getAttribute("AuthToken");

        if(authToken.getCreatedAt().plusSeconds(authToken.getAccessTokenTime()).isBefore(LocalDateTime.now())){
            if(authToken.getCreatedAt().plusSeconds(authToken.getRefreshTokenTime()).isAfter(LocalDateTime.now())){
                httpResponse.sendRedirect("/oauth/renew/kakao?token="+authToken.getId());
                return;
            }
            tokenRepository.deleteById(authToken.getId());
            httpResponse.sendRedirect("/home");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
