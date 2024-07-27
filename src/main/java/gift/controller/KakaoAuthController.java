package gift.controller;

import gift.dto.ApiResponse;
import gift.service.KakaoAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/oauth/kakao")
    public ApiResponse<String> kakaoLogin(@RequestParam("code") String authorizationCode) {
        try {
            String accessToken = kakaoAuthService.getAccessToken(authorizationCode);
            return new ApiResponse<>(true, "Access token retrieved successfully", accessToken, null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to retrieve access token", null, "ERROR_CODE");
        }
    }
}