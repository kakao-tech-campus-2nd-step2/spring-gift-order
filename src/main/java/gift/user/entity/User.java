package gift.user.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.wish.entity.Wish;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Set;
import java.util.regex.Pattern;

@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"email"}, name = "uk_users")
)
public class User {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Wish> wishes;

    public User(String email, String password, Set<UserRole> userRoles) {
        validateEmail(email);
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }

    protected User() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<UserRole> getRoles() {
        return userRoles;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    private void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }
    }

}
