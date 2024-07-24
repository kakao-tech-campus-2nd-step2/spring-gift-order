package gift.annotation;

import gift.jwt.JWTService;
import gift.user.KakaoUser;
import gift.user.User;
import gift.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;
    private final JWTService jwtService;

    public LoginUserArgumentResolver(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean assignableFrom = KakaoUser.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && assignableFrom;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = request.getHeader("Authorization");

        if(accessToken == null) return null;
        return new KakaoUser(jwtService.getAccessToken(accessToken), userService.getUserById(Long.parseLong(jwtService.getClaims(accessToken))));
    }
}
