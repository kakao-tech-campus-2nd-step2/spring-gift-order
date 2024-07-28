package gift.users.kakao;

import gift.error.KakaoOrderException;
import gift.users.user.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class KakaoOrderApiController {

    private final KakaoOrderService kakaoOrderService;
    private final UserService userService;

    public KakaoOrderApiController(KakaoOrderService kakaoOrderService, UserService userService) {
        this.kakaoOrderService = kakaoOrderService;
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<KakaoOrderDTO> kakaoOrder(@PathVariable("userId") long userId,
        @Valid @RequestBody KakaoOrderDTO kakaoOrderDTO){

        LocalDateTime currentDateTime = LocalDateTime.now();
        String orderDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        if(!userService.findSns(userId).equals("kakao")){
            throw new KakaoOrderException("카카오 유저만 이용할 수 있는 서비스입니다.");
        }

        KakaoOrderDTO kakaoOrderResponse = kakaoOrderService.kakaoOrder(userId, kakaoOrderDTO, orderDateTime);
        return ResponseEntity.ok(kakaoOrderResponse);
    }
}
