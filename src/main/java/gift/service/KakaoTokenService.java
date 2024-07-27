package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.entity.KakaoToken;
import gift.exception.CustomException;
import gift.repository.KakaoTokenRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class KakaoTokenService {
    
    private KakaoTokenRepository kakaoTokenRepository;
    private JwtUtil jwtUtil;

    public KakaoTokenService(KakaoTokenRepository kakaoTokenRepository, JwtUtil jwtUtil){
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void saveKakaoToken(String email, String accessToken){

        if(kakaoTokenRepository.findByEmail(email).isEmpty()){
            KakaoToken kakaoToken = new KakaoToken(email, accessToken);
            kakaoTokenRepository.save(kakaoToken);
        }
    }

    public String findKakaoToken(String token){

        String email = (String)jwtUtil.extractAllClaims(token).get("email");
        
        KakaoToken kakaoToken = kakaoTokenRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("KakaoToken is not exists", HttpStatus.NOT_FOUND));
        
        return kakaoToken.getAccessToken();
    }
}
