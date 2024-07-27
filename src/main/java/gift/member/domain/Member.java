package gift.member.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = true, unique = true, length = 255)
    private Long kakaoId;

    protected Member() {
    }

    public Member(String email, String password) {
        this(null, email, password, null);
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, null);
    }

    public Member(Long id, String email, String password, Long kakaoId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
    }

    public static Member ofKakao(Long kakaoId, String email) {
        return new Member(null, email, null, kakaoId);
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

    public Long getKakaoId() {
        return kakaoId;
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
