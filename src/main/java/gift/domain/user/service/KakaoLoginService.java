package gift.domain.user.service;

import gift.auth.dto.Token;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.KakaoUserInfo;
import gift.domain.user.dto.UserDto;
import gift.domain.user.dto.UserLoginDto;
import java.util.Optional;
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

        Optional<UserDto> userDto = userService.findByEmail(email);
        if (userDto.isEmpty()) {
            return signUp(name, email);
        }

        return userService.login(new UserLoginDto(email, userDto.get().password()));
    }

    private Token signUp(String name, String email) {
        UserDto userDto = new UserDto(null, name, email, "kakao", null);
        return userService.signUp(userDto);
    }
}
