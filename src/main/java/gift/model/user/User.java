package gift.model.user;

import gift.common.enums.LoginType;
import gift.exception.InvalidUserException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoginType loginType;


    public User() {
    }

    public User(String email, String password, LoginType loginType) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void checkLoginType(LoginType loginType) {
        if (this.loginType != loginType) {
            throw new InvalidUserException("유효하지 않은 로그인상태입니다.");
        }
    }

}

