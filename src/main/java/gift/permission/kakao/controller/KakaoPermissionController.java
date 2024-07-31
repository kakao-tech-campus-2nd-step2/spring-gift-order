package gift.permission.kakao.controller;

import gift.permission.kakao.service.KakaoPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class KakaoPermissionController {

    private final KakaoPermissionService kakaoPermissionService;

    public KakaoPermissionController(KakaoPermissionService kakaoPermissionService) {
        this.kakaoPermissionService = kakaoPermissionService;
    }

    @GetMapping("/login/kakao")
    @Operation
    public RedirectView getKakaoLoginPage() {
        return kakaoPermissionService.kakaoAuthorize();
    }
}
