package gift.domain.user.service;

import gift.auth.dto.Token;
import gift.domain.user.dto.UserResponse;
import gift.exception.InvalidUserInfoException;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.KakaoUserInfo;
import gift.domain.user.dto.UserRequest;
import gift.domain.user.dto.UserLoginRequest;
import org.springframework.stereotype.Service;

@Service
public class KakaoLoginService {

    private final KakaoApiProvider kakaoApiProvider;
    private final UserService userService;

    public KakaoLoginService(KakaoApiProvider kakaoApiProvider, UserService userService) {
        this.kakaoApiProvider = kakaoApiProvider;
        this.userService = userService;
    }

    public String getAuthCodeUrl() {
        return kakaoApiProvider.getAuthCodeUrl();
    }

    public Token login(String code) {
        String accessToken = kakaoApiProvider.getToken(code).accessToken();
        kakaoApiProvider.validateAccessToken(accessToken);
        KakaoUserInfo userInfo = kakaoApiProvider.getUserInfo(accessToken);

        String email = userInfo.kakaoAccount().email();
        String name = userInfo.kakaoAccount().profile().nickname();

        try {
            UserResponse userResponse = userService.readByEmail(email);
            return userService.login(new UserLoginRequest(email, userResponse.password()));
        } catch (InvalidUserInfoException e) {
            return signUp(name, email);
        }
    }

    private Token signUp(String name, String email) {
        UserRequest userRequest = new UserRequest(name, email, "kakao");
        return userService.signUp(userRequest);
    }
}
