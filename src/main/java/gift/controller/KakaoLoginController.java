package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ApiResponse;
import gift.model.HttpResult;
import gift.model.Member;
import gift.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class KakaoLoginController {

    private MemberService memberService;


    public KakaoLoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String redirectedPageForAuthCode(
        @RequestParam(value = "code", required = false) String code,
        Model model, HttpSession session) {
        model.addAttribute("message", "homepage");
        if (code != null) {
            session.setAttribute("kakao_auth_code", code);
            model.addAttribute("message", "인가 코드 확인");
            return "redirect:/kakao/login";
        }
        return "kakaologin";
    }

    @GetMapping("/kakao/page")
    public String getKakaoPage() {
        return "kakaologin";
    }

    @GetMapping("/kakao/authcode")
    public String getAuthentificationCodeWithKakao(
    ) {
        return "redirect:" + memberService.getAuthentificationCode();
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<ApiResponse> loginWithKakao(HttpSession session)
        throws JsonProcessingException {
        String authCode = (String) session.getAttribute("kakao_auth_code");
        if (authCode == null) {
            return new ResponseEntity<>(
                new ApiResponse(HttpResult.ERROR, "인가 코드 에러", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<String> response = memberService.getResponseEntity(authCode);
        String accessToken = memberService.getAccessToken(response.getBody());
        session.removeAttribute("kakao_auth_code");
        var headers = getHttpHeaders(accessToken);
        var responseEntity = getHttpResponse(headers);
        var kakaoId = getKakaoId(responseEntity);

        try {
            var kakaoMemberJws = memberService.kakaoLogin(kakaoId);
            var apiResponse = new ApiResponse(HttpResult.OK, "카카오 멤버 ID 확인", HttpStatus.OK);
            if (kakaoMemberJws.isPresent()) { // 이미 있는 계정
                return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
            }
            memberService.registerKakaoMember(
                new Member("email@email.com", "pw", String.valueOf(kakaoId)));
            return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
        } catch (NumberFormatException e) {
            System.out.println("Error processing kakao login: " + e.getMessage());
            return new ResponseEntity<>(
                new ApiResponse(HttpResult.ERROR, "카카오 로그인 처리 중 오류 발생",
                    HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long getKakaoId(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("id").asLong();
    }

    public HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    public ResponseEntity<String> getHttpResponse(HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
    }
}
