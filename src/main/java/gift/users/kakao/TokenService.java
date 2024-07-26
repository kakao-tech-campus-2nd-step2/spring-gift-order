package gift.users.kakao;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(Long userId, KakaoTokenDTO kakaoTokenDTO){
        if(tokenRepository.existsByUserId(userId)){
            return;
        }
        Token token = new Token(userId, "kakao", kakaoTokenDTO.accessToken(),
            kakaoTokenDTO.expiresIn(), kakaoTokenDTO.refreshToken(), kakaoTokenDTO.refreshTokenExpiresIn());
        tokenRepository.save(token);
    }

    public String findToken(long userId, String sns){
        return tokenRepository.findByUserIdAndSns(userId, sns).getAccessToken();
    }
}
