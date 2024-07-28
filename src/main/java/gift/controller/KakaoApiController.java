package gift.controller;

import gift.service.KakaoApiService;
import gift.service.MemberService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/kakao")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        Map<String, String> memberCredential = kakaoApiService.createKakaoMember(code);
        return ResponseEntity.status(HttpStatus.OK).body(memberCredential);
    }

    @ExceptionHandler(RestClientException.class)
    public String handleModelValidationExceptions(RestClientException ex, Model model) {
        String errorMessage = ex.getMessage();
        model.addAttribute("Error", errorMessage);
        return "error";
    }
}