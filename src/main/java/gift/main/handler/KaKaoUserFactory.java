package gift.main.handler;

import gift.main.dto.KakaoProfileRequest;
import gift.main.entity.Role;
import gift.main.entity.User;

public class KaKaoUserFactory {

    private static String PASSWORD = "kakao2024";
    public static User convertKakaoUserToUser(KakaoProfileRequest kakaoProfileRequest) {
        //String name, String email, String password, String role
        String name = kakaoProfileRequest.nickname();
        String email = kakaoProfileRequest.idToString() + kakaoProfileRequest.nickname();
        String password = PASSWORD;
        Role role = Role.USER;

        return new User(name, email, password, role);
    }
}