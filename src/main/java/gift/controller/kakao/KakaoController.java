package gift.controller.kakao;

import gift.domain.order.OrderRequest;
import gift.domain.user.User;
import gift.validation.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import gift.service.kakao.KakaoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @PostMapping("/login")
    public Map<String, Object> kakaoLogin(@RequestParam("code") String authorizationCode) {
        return kakaoService.kakaoLogin(authorizationCode);
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createOrder(@LoginMember User loginUser,
                                         @RequestParam("accessToken") String accessToken,
                                         @RequestBody OrderRequest orderRequest) {
        Map<String, Object> response = kakaoService.createOrder(loginUser, accessToken, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}