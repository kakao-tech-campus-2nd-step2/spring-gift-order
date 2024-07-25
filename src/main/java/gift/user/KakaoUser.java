package gift.user;

import gift.wishList.WishList;
import jakarta.persistence.*;
import org.springframework.data.util.Lazy;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "KAKAOUSERS")
public class KakaoUser {
    @Id
    Long id;
    @Column
    String accessToken;
    @Column
    String refreshToken;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "kakaouser", orphanRemoval = true)
    private List<WishList> wishLists = new ArrayList<>();


    public KakaoUser() {
    }

    public KakaoUser(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
