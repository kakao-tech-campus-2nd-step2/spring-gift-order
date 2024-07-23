package gift.Model.Entity;

import gift.Model.Value.Email;
import gift.Model.Value.Password;
import jakarta.persistence.*;

@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private Email email;

    @Embedded
    @Column(nullable = false)
    private Password password;

    protected Member() {}

    public Member(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password) {
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public void update(Email email, Password password){
        this.email = email;
        this.password = password;
    }

    public void update(String email, String password) {
        update(new Email(email), new Password(password));
    }
}
