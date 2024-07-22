package gift.Model;

import jakarta.persistence.*;

import java.util.regex.Pattern;

@Entity
public class Member {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    protected Member() {}

    public Member(String email, String password) {
        validateEmail(email);
        validatePassword(password);

        this.email = email;
        this.password = password;
    }

    public void validateEmail(String email){
        if (email == null|| email.isBlank())
            throw new IllegalArgumentException("이메일 값은 필수입니다.");
        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("이메일 양식을 다시 확인해주세요");
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("패스워드 값은 필수입니다");
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
