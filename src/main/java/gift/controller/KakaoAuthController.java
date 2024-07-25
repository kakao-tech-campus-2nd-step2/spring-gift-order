package gift.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.service.KakaoAuthService;

@RestController
@RequestMapping("/kakao")
public class KakaoAuthController {
	
	private final KakaoAuthService kakaoAuthService;
	
	public KakaoAuthController(KakaoAuthService kakaoAuthService) {
		this.kakaoAuthService = kakaoAuthService;
	}
	
	@GetMapping("/redirect")
	public ResponseEntity<Map<String, String>> kakaoRedirect(@RequestParam("code") String authorizationCode) {
		Map<String, String> accessToken = kakaoAuthService.getAccessToken(authorizationCode);
		return ResponseEntity.status(HttpStatus.OK).body(accessToken);
	}
}
