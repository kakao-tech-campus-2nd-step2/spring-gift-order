package gift.main.handler;

import gift.main.dto.KakaoProfile;
import gift.main.entity.Role;
import gift.main.entity.User;

public class KaKaoUserFactory {

    private static String PASSWORD = "kakao2024";
    public static User convertKakaoUserToUser(KakaoProfile kakaoProfile) {
        //String name, String email, String password, String role
        String name = kakaoProfile.properties().nickname();
        String email = kakaoProfile.id().toString() + kakaoProfile.properties().nickname();
        String password = PASSWORD;
        Role role = Role.USER;

        return new User(name, email, password, role);
    }
}