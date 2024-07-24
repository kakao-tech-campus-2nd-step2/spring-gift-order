package gift.domain.member;

import gift.domain.Wish;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private MemberRole role;

    @Column(unique = true)
    private Long kakaoId;

    private String accessToken;
    private String refreshToken;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MemberRole getRole() {
        return role;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public Member(Long id, String name, String email, String password, MemberRole role, Long kakaoId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public static class MemberBuilder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private MemberRole role;
        private Long kakaoId;

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder role(MemberRole role) {
            this.role = role;
            return this;
        }

        public MemberBuilder kakaoId(Long kakaoId) {
            this.kakaoId = kakaoId;
            return this;
        }

        public Member build() {
            return new Member(id, name, email, password, role, kakaoId);
        }
    }

}
