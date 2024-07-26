package gift.domain.user;

import gift.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected User() {
    }

    public User(Long id, String email, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
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

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(email, user.email) &&
               Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
