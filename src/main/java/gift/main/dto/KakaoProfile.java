package gift.main.dto;

import gift.main.entity.Role;
import org.springframework.beans.factory.annotation.Value;

public record KakaoProfile(Long id, Properties properties, @Value("{spring.kakao.password}") String password) {

    public String name() {
        return properties.nickname();
    }

    public String email() {
        return id().toString() + properties.nickname();
    }

    public String password() {
        return password;
    }

    public Role role() {
        return Role.USER;
    }


}
