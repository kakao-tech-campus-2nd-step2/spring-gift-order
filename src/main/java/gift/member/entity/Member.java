package gift.member.entity;

import gift.member.dto.MemberDto;
import gift.wishlist.entity.Wish;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private final List<Wish> wishList = new ArrayList<>();

    private String kakaoAccessToken;

    private String kakaoRefreshToken;

    protected Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email,
                  String password,
                  String kakaoAccessToken,
                  String kakaoRefreshToken) {
        this.email = email;
        this.password = password;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
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

    public List<Wish> getWishList() {
        return wishList;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public String getKakaoRefreshToken() {
        return kakaoRefreshToken;
    }

    public void update(MemberDto memberDto) {
        email = memberDto.email();
        password = memberDto.password();
    }

    public void updateTokens(String accessToken, String refreshToken) {
        kakaoAccessToken = accessToken;
        kakaoRefreshToken = refreshToken;
    }

}
