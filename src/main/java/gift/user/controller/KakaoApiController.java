package gift.user.controller;

import gift.user.service.KakaoApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class KakaoApiController {

  private final KakaoApiService kakaoApiService;

  public KakaoApiController(KakaoApiService kakaoApiService) {
    this.kakaoApiService = kakaoApiService;
  }

  @GetMapping("/kakao/callback")
  public Mono<String> authenticate(@RequestParam String code) {
    System.out.println("인가 코드 받음: " + code);
    return kakaoApiService.getKakaoToken(code)
        .flatMap(token -> {
          String accessToken = parseAccessToken(token);
          return kakaoApiService.getKakaoUserInfo(accessToken);
        })
        .flatMap(userInfo -> {
          return kakaoApiService.saveOrUpdateUser(userInfo)
              .then(Mono.just("카카오 로그인 성공"));
        })
        .onErrorResume(e -> {
          return Mono.just("카카오 로그인 중 오류가 발생했습니다: " + e.getMessage());
        });
  }

  private String parseAccessToken(String token) {
    int startIndex = token.indexOf("access_token\":\"") + "access_token\":\"".length();
    int endIndex = token.indexOf("\"", startIndex);
    return token.substring(startIndex, endIndex);
  }
}
