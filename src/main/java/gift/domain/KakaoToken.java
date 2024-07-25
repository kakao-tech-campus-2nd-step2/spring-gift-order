package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "kakao_token")
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "member_id", unique = true)
    private Member member;
}
