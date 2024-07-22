package gift.Controller;

import gift.DTO.KakaoJwtToken;
import gift.DTO.KakaoMember;
import gift.DTO.KakaoMemberDto;
import gift.Service.KakaoMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoMemberController {
  private KakaoMemberService kakaoMemberService;

  public KakaoMemberController(KakaoMemberService kakaoMemberService){
    this.kakaoMemberService=kakaoMemberService;
  }

  @PostMapping("/token")
  public ResponseEntity<KakaoJwtToken> getToken(@RequestBody KakaoMemberDto kakaoMemberDto){
    KakaoJwtToken kakaoJwtToken = kakaoMemberService.getToken(kakaoMemberDto);
    return ResponseEntity.ok(kakaoJwtToken);
  }

}
