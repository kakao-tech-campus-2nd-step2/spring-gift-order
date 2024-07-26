package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoLoginResponse;
import gift.domain.Member;
import gift.domain.MemberResponse;
import gift.service.KakaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

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
