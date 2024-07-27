package gift.Service;

import gift.DTO.KakaoJwtToken;
import gift.KakaoApi;
import gift.Repository.KakaoJwtTokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Service
public class KakaoMemberService {

  private KakaoJwtTokenRepository kakaoJwtTokenRepository;
  private KakaoApi kakaoApi;
  static Dotenv dotenv = Dotenv.configure().load();
  private static final String API_KEY = dotenv.get("API_KEY");
  private static final String URL = "https://kauth.kakao.com/oauth/token";

  public KakaoMemberService(KakaoJwtTokenRepository kakaoJwtTokenRepository, KakaoApi kakaoApi) {
    this.kakaoJwtTokenRepository = kakaoJwtTokenRepository;
    this.kakaoApi = kakaoApi;
  }

  public KakaoJwtToken getToken(String autuhorizationKey) {



    KakaoJwtToken kakaoJwtToken = kakaoApi.kakaoLoginApiPost(URL, API_KEY,autuhorizationKey);
    kakaoJwtTokenRepository.save(kakaoJwtToken);
    return kakaoJwtToken;

  }
}
