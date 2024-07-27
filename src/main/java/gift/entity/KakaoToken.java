package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "kakao_token")
public class KakaoToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String accessToken;

    public KakaoToken(String email, String accessToken){
        this.email = email;
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }
    
    
}
