package gift;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoJwtToken;
import gift.Exception.UnauthorizedException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLoginApi {
  RestClient restClient = RestClient.builder().build();

  public KakaoJwtToken kakaoLoginApiPost(String url, LinkedMultiValueMap<String,String> body){
    String response = restClient.post()
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

      KakaoJwtToken kakaoJwtToken = new KakaoJwtToken(accessToken, tokenType, refreshToken,
        expiresIn, scope,
        refreshTokenExpiresIn);

      return kakaoJwtToken;

    } catch (Exception e) {
      throw new UnauthorizedException(
        "KOE320 : 동일한 인가 코드를 두 번 이상 사용하거나, 이미 만료된 인가 코드를 사용한 경우, 혹은 인가 코드를 찾을 수 없는 경우입니다.");
    }
  }
}
