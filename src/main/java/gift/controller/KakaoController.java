package gift.controller;

import gift.domain.Member;
import gift.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController {
    private KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService){
        this.kakaoService = kakaoService;
    }

    @GetMapping("/getcode")
    public RedirectView getUserAgree(){
        URI uri = kakaoService.makeUri();
        return new RedirectView(uri.toString());
    }

    @GetMapping("/code")
    public ResponseEntity<Member> getUserInfomation(
            @RequestParam("code") String code
    ){
        Member member = kakaoService.getToken(code);
        System.out.println(member);
        return ResponseEntity.ok().body(member);
    }

}
