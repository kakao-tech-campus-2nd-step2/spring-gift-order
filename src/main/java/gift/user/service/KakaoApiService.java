package gift.user.service;

import gift.user.entity.User;
import gift.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
public class KakaoApiService {

  private final WebClient webClient;
  private final UserRepository userRepository;

  @Value("${kakao.client-id}")
  private String clientId;

  @Value("${kakao.redirect-uri}")
  private String redirectUri;

  public KakaoApiService(WebClient.Builder webClientBuilder, UserRepository userRepository) {
    this.webClient = webClientBuilder.baseUrl("https://kauth.kakao.com").build();
    this.userRepository = userRepository;
  }

  public Mono<String> getKakaoToken(String authorizationCode) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_uri", redirectUri);
    body.add("code", authorizationCode);

    return webClient.post()
        .uri("/oauth/token")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData(body))
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<Map<String, Object>> getKakaoUserInfo(String accessToken) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("property_keys", "[\"kakao_account.email\"]");

    return webClient.post()
        .uri("https://kapi.kakao.com/v2/user/me")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .body(BodyInserters.fromFormData(body))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
  }

  public Mono<Void> saveOrUpdateUser(Map<String, Object> userInfo) {
    return Mono.fromRunnable(() -> {
      Long kakaoId = (Long) userInfo.get("id");
      String email = "kakao_" + kakaoId + "@kakao.com";

      Optional<User> userOptional = userRepository.findByEmail(email);
      User user;
      if (userOptional.isPresent()) {
        user = userOptional.get();
      } else {
        user = new User();
        user.setEmail(email);
      }

      userRepository.save(user);
    });
  }
}
