package gift.token.resolver;

import gift.global.annotation.UserId;
import gift.token.component.TokenComponent;
import gift.token.model.TokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

// 사용자가 보내진 않지만 보내야만 하는 것들을 미리 처리하는 resolver
@Component
public class UserIdResolver extends TokenManager implements HandlerMethodArgumentResolver {

    public UserIdResolver() {
        super();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isUserAnnotation = parameter.hasParameterAnnotation(UserId.class);
        boolean isLong = parameter.getParameterType().equals(Long.class);

        return isUserAnnotation && isLong;
    }

    @Override
    public Long resolveArgument(MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return getUserId(token);
    }


}