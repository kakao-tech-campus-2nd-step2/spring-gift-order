package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;

@Entity
@Table(name = "`user`", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "kakaoId"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COMMENT 'User Email'")
    private String email;

    @Column(columnDefinition = "VARCHAR(255) COMMENT 'User Password'")
    private String password;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COMMENT 'Kakao ID'")
    private String kakaoId;

    @Column(columnDefinition = "VARCHAR(255) COMMENT 'User Nickname'")
    private String nickname;

    protected User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        validateEmail(email);
        validatePassword(password);
        this.nickname = null;
        this.kakaoId = null;
    }

    public User(Long kakaoId, String nickname) {
        this.kakaoId = kakaoId.toString();
        this.nickname = nickname;
        this.email = null;
        this.password = null;
    }

    public void update(String email, String password) {
        validateEmail(email);
        validatePassword(password);
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (email != null && (email.trim().isEmpty() || !email.contains("@"))) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        }
    }

    private void validatePassword(String password) {
        if (password != null && password.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public boolean isPasswordCorrect(String password) {
        return this.password != null && this.password.equals(password);
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

    public String getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return nickname;
    }
}
