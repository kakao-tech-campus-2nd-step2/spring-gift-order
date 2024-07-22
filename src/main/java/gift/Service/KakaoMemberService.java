package gift.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoJwtToken;
import gift.DTO.KakaoMemberDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoMemberService {

  RestClient restClient = RestClient.builder().build();

  public KakaoJwtToken getToken(KakaoMemberDto kakaoMemberDto) {
    var url = "https://kauth.kakao.com/oauth/token";
    var body = new LinkedMultiValueMap<String, String>();
    String clientId = kakaoMemberDto.getClientId();
    String code = kakaoMemberDto.getCode();

    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_url", "http://localhost:8080");
    body.add("code", code);
    var response = restClient.post()
      .uri(url)
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

      return new KakaoJwtToken(accessToken, tokenType, refreshToken, expiresIn, scope,
        refreshTokenExpiresIn);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
