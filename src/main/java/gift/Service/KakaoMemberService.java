package gift.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoJwtToken;
import gift.Exception.UnauthorizedException;
import gift.KakaoLoginApi;
import gift.Repository.KakaoJwtTokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoMemberService {

  private KakaoJwtTokenRepository kakaoJwtTokenRepository;
  private KakaoLoginApi kakaoLoginApi;
  static Dotenv dotenv = Dotenv.configure().load();
  private static final String API_KEY = dotenv.get("API_KEY");
  private static final String URL = "https://kauth.kakao.com/oauth/token";

  public KakaoMemberService(KakaoJwtTokenRepository kakaoJwtTokenRepository, KakaoLoginApi kakaoLoginApi) {
    this.kakaoJwtTokenRepository = kakaoJwtTokenRepository;
    this.kakaoLoginApi=kakaoLoginApi;
  }

  public KakaoJwtToken getToken(String autuhorizationKey) {
    var body = new LinkedMultiValueMap<String, String>();

    body.add("grant_type", "authorization_code");
    body.add("client_id", API_KEY);
    body.add("redirect_url", "http://localhost:8080");
    body.add("code", autuhorizationKey);

    KakaoJwtToken kakaoJwtToken = kakaoLoginApi.kakaoLoginApiPost(URL, body);
    return kakaoJwtToken;

  }
}
