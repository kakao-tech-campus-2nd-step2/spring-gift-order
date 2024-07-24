package gift.main.dto;

import gift.main.entity.Role;

public record KakaoUser(KakaoProfile kakaoProfile, String password) {
    public String name() {
        return kakaoProfile.properties().nickname();
    }

    public String email() {
        return kakaoProfile.id().toString() + kakaoProfile.properties().nickname();
    }

    public String password() {
        return password;
    }

    public Role role() {
        return Role.USER;
    }

}

