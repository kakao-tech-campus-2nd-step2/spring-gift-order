package gift.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(length = 72)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column
    private String providerId;

    protected User() {
    }

    public User(String email, AuthProvider authProvider, String providerId) {
        this.email = email;
        this.authProvider = authProvider;
        this.providerId = providerId;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.authProvider = AuthProvider.LOCAL;
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

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public String getProviderId() {
        return providerId;
    }

    public enum AuthProvider {
        LOCAL, KAKAO
    }
}