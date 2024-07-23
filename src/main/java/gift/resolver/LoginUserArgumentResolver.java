package gift.resolver;

import gift.annotation.LoginMember;
import gift.dto.request.MemberRequest;
import gift.dto.response.KakaoMemberResponse;
import gift.dto.response.MemberResponse;
import gift.service.JwtUtil;
import gift.service.KakaoMemberService;
import gift.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final KakaoMemberService kakaoMemberService;

    public LoginUserArgumentResolver(MemberService memberService, JwtUtil jwtUtil, KakaoMemberService kakaoMemberService) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.kakaoMemberService = kakaoMemberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");
        String token = authHeader.substring(7);

        if (kakaoMemberService.checkTokenExists(token)) {
            KakaoMemberResponse kakaoMemberDto= kakaoMemberService.findByAccessToken(token);
            MemberResponse memberDto = memberService.findByEmail(kakaoMemberDto.email());
            return new MemberRequest(memberDto.id(), memberDto.email(), memberDto.password());
        }

        String userEmail = jwtUtil.extractEmail(authHeader.substring(7));
        MemberResponse memberDto = memberService.findByEmail(userEmail);
        return new MemberRequest(memberDto.id(),memberDto.email(),memberDto.password());
    }
}
