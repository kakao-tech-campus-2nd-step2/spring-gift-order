package gift.domain.user.service;

import gift.auth.dto.Token;
import gift.auth.oauth.kakao.KakaoApiProvider;
import gift.domain.user.dto.UserDto;
import gift.domain.user.dto.UserLoginDto;
import java.util.Map;
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
        String accessToken = kakaoApiProvider.getAccessToken(code);
        kakaoApiProvider.validateAccessToken(accessToken);
        Map<String, String> userInfo = kakaoApiProvider.getUserInfo(accessToken);

        String email = userInfo.get("email");
        Optional<UserDto> userDto = userService.findByEmail(email);

        if (userDto.isEmpty()) {
            return signUp(userInfo);
        }

        return userService.login(new UserLoginDto(email, userDto.get().password()));
    }

    private Token signUp(Map<String, String> userInfo) {
        UserDto userDto = new UserDto(null, userInfo.get("name"), userInfo.get("email"), "kakao", null);
        return userService.signUp(userDto);
    }
}
