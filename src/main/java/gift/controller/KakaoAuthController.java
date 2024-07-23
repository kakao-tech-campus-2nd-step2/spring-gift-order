package gift.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.service.KakaoAuthService;

@RestController
@RequestMapping("/kakao")
public class KakaoAuthController {
	
	private final KakaoAuthService kakaoAuthService;
	
	public KakaoAuthController(KakaoAuthService kakaoAuthService) {
		this.kakaoAuthService = kakaoAuthService;
	}
	
	@PostMapping("/redirect")
	public ResponseEntity<Map<String, String>> kakaoRedirect(@RequestBody Map<String, String> request) {
		Map<String, String> accessToken = kakaoAuthService.getAccessToken(request.get("code"));
		return ResponseEntity.status(HttpStatus.OK).body(accessToken);
	}
}
