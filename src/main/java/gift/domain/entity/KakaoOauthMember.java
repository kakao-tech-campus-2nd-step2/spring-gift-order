package gift.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class KakaoOauthMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer kakaoIdentifier;

    @OneToOne
    @JoinColumn(unique = true)
    private Member member;

    protected KakaoOauthMember() {
    }

    public KakaoOauthMember(Integer kakaoIdentifier, Member member) {
        this.kakaoIdentifier = kakaoIdentifier;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Integer getKakaoIdentifier() {
        return kakaoIdentifier;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "KakaoOauthMember{" +
            "id=" + id +
            ", kakaoIdentifier=" + kakaoIdentifier +
            ", member=" + member +
            '}';
    }
}
