package gift.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoJwtToken;
import gift.Exception.UnauthorizedException;
import gift.Repository.KakaoJwtTokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

;

@Service
public class KakaoMemberService {

  private KakaoJwtTokenRepository kakaoJwtTokenRepository;
  RestClient restClient = RestClient.builder().build();
  static Dotenv dotenv = Dotenv.configure().load();
  private static final String API_KEY = dotenv.get("API_KEY");
  private static final String URL = "https://kauth.kakao.com/oauth/token";

  public KakaoMemberService(KakaoJwtTokenRepository kakaoJwtTokenRepository) {
    this.kakaoJwtTokenRepository = kakaoJwtTokenRepository;
  }

  public KakaoJwtToken getToken(String autuhorizationKey) {
    var body = new LinkedMultiValueMap<String, String>();
    String clientId = API_KEY;
    String code = autuhorizationKey;

    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_url", "http://localhost:8080");
    body.add("code", code);
    var response = restClient.post()
      .uri(URL)
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(body)
      .retrieve()
      .body(String.class);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonNode = objectMapper.readTree(response);
      String accessToken = jsonNode.get("access_token").asText();
      String tokenType = jsonNode.get("token_type").asText();
      String refreshToken = jsonNode.get("refresh_token").asText();
      int expiresIn = jsonNode.get("expires_in").asInt();
      String scope = jsonNode.get("scope").asText();
      int refreshTokenExpiresIn = jsonNode.get("refresh_token_expires_in").asInt();

      KakaoJwtToken kakaoJwtToken = new KakaoJwtToken(accessToken, tokenType, refreshToken,
        expiresIn, scope,
        refreshTokenExpiresIn);
      kakaoJwtTokenRepository.save(kakaoJwtToken);

      return kakaoJwtToken;

    } catch (Exception e) {
      throw new UnauthorizedException(
        "KOE320 : 동일한 인가 코드를 두 번 이상 사용하거나, 이미 만료된 인가 코드를 사용한 경우, 혹은 인가 코드를 찾을 수 없는 경우입니다.");
    }
  }
}
