package gift.Controller;

import gift.DTO.KakaoJwtToken;
import gift.Service.KakaoMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoMemberController {

  private final KakaoMemberService kakaoMemberService;

  public KakaoMemberController(KakaoMemberService kakaoMemberService) {
    this.kakaoMemberService = kakaoMemberService;
  }

  @PostMapping("/token")
  public ResponseEntity<KakaoJwtToken> getToken(@RequestBody String authorizationKey) {
    KakaoJwtToken kakaoJwtToken = kakaoMemberService.getToken(authorizationKey);
    return ResponseEntity.ok(kakaoJwtToken);
  }

}
