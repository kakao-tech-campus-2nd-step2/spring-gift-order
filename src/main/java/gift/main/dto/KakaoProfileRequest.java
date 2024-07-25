package gift.main.dto;

import gift.main.entity.Role;
import gift.main.entity.User;

public record KakaoProfileRequest(Long id, Properties properties) {

    public String nickname(){
        return properties().nickname;
    }
    public String idToString(){
        return id.toString();
    }
    record Properties(String nickname) {
    }

    public  User convertToUser() {
        String name = this.properties.nickname;
        String email = idToString() + this.properties.nickname;
        String password = "kakao2024";
        Role role = Role.USER;

        return new User(name, email, password, role);
    }

}
