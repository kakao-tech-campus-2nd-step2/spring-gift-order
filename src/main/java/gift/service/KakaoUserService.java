package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.KakaoProperties;
import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import gift.domain.User;
import gift.security.JwtTokenProvider;
import gift.security.KakaoTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoUserService {
    private final KakaoProperties kakaoProperties;
    private final KakaoTokenProvider kakaoTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${kakao.loginUrl}")
    private String loginUrl;
    @Value("${kakao.messageUrl}")
    private String messageUrl;

    public KakaoUserService(
            KakaoTokenProvider kakaoTokenProvider,
            JwtTokenProvider jwtTokenProvider,
            KakaoProperties kakaoProperties
    ) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoProperties = kakaoProperties;
    }

    @Transactional
    public Token login(String code) throws JsonProcessingException {
        String token = kakaoTokenProvider.getToken(code);
        User kakaoUser = kakaoTokenProvider.getKakaoUserInfo(token);

        return jwtTokenProvider.makeToken(new UserRequest(
                kakaoUser.getUserId(), kakaoUser.getEmail(), kakaoUser.getPassword()
        ));
    }

    public String makeLoginUrl(){
        loginUrl += "?scope=talk_message";
        loginUrl += "&response_type=code";
        loginUrl += "&redirect_uri=" + kakaoProperties.getRedirectUrl();
        loginUrl += "&client_id=" + kakaoProperties.getClientId();
        return loginUrl;
    }

    public void messageToMe(){

    }
}
