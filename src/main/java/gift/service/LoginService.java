package gift.service;

import gift.util.KakaoApiUtil;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    KakaoApiUtil kakaoApiUtil;

    public LoginService(KakaoApiUtil kakaoApiUtil) {
        this.kakaoApiUtil = kakaoApiUtil;
    }

    public String getAccessTokenByCode(String code){
        return kakaoApiUtil.getAccessToken(code);
    }

}
