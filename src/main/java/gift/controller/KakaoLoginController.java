package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ApiResponse;
import gift.model.HttpResult;
import gift.model.Member;
import gift.service.MemberService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class KakaoLoginController {

    private MemberService memberService;
    private String accessToken;
    private String authCode;

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
            authCode = code;
            model.addAttribute("message", "인가 코드 확인");
            System.out.println(code);
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
    public ResponseEntity<ApiResponse> loginWithKakao(HttpSession session) throws JsonProcessingException {
        String authCode = (String) session.getAttribute("kakao_auth_code");
        if (authCode == null) {
            return new ResponseEntity<>(
                new ApiResponse(HttpResult.ERROR, "인가 코드 에러", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<String> response = memberService.getResponseEntity(authCode);
        String accessToken = memberService.getAccessToken(response.getBody());
        System.out.println(accessToken);
        var headers = getHttpHeaders(accessToken);
        var responseEntity = getHttpResponse(headers);

        Long kakaoId = getKakaoId(responseEntity);
        String kakaoEmail = getKakaoEmail(responseEntity);

        try {
            Optional<String> jwtToken = memberService.loginOrRegisterKakaoUser(kakaoId, kakaoEmail);
            if (jwtToken.isPresent()) {
                ApiResponse apiResponse = new ApiResponse(HttpResult.OK, "카카오 로그인 성공", HttpStatus.OK);
                System.out.println(jwtToken.get());
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                    new ApiResponse(HttpResult.ERROR, "카카오 로그인 실패", HttpStatus.UNAUTHORIZED),
                    HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                new ApiResponse(HttpResult.ERROR, "카카오 로그인 처리 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/kakao/gift/self")
    public ResponseEntity<ApiResponse> giftForOneSelf(HttpSession session)
        throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Create the template object as a JSON string
        var templateObject = getString();

        MultiValueMap<String, String> bodyTemplate = new LinkedMultiValueMap<>();
        bodyTemplate.add("template_object", templateObject);

        var headers = getHttpHeadersForSelfMessage(accessToken);
        var responseEntity = getHttpSendSelfMessage(bodyTemplate, headers);

        String responseBody = responseEntity.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        var resultCode = jsonNode.get("result_code").asText();
        if (resultCode.equals("0")) {
            return new ResponseEntity<>(new ApiResponse(HttpResult.OK, "성공", HttpStatus.OK),
                HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpResult.ERROR, "실패", HttpStatus.BAD_REQUEST),
            HttpStatus.BAD_REQUEST);
    }

    private static String getString() {
        String templateObject = "{"
            + "\"object_type\": \"feed\","
            + "\"content\": {"
            + "  \"title\": \"오늘의 디저트\","
            + "  \"description\": \"아메리카노, 빵, 케익\","
            + "  \"image_url\": \"https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg\","
            + "  \"image_width\": 640,"
            + "  \"image_height\": 640,"
            + "  \"link\": {"
            + "    \"web_url\": \"http://www.daum.net\","
            + "    \"mobile_web_url\": \"http://m.daum.net\","
            + "    \"android_execution_params\": \"contentId=100\","
            + "    \"ios_execution_params\": \"contentId=100\""
            + "  }"
            + "},"
            + "\"item_content\": {"
            + "  \"profile_text\": \"Kakao\","
            + "  \"profile_image_url\": \"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\","
            + "  \"title_image_url\": \"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\","
            + "  \"title_image_text\": \"Cheese cake\","
            + "  \"title_image_category\": \"Cake\","
            + "  \"items\": ["
            + "    {\"item\": \"Cake1\", \"item_op\": \"1000원\"},"
            + "    {\"item\": \"Cake2\", \"item_op\": \"2000원\"},"
            + "    {\"item\": \"Cake3\", \"item_op\": \"3000원\"},"
            + "    {\"item\": \"Cake4\", \"item_op\": \"4000원\"},"
            + "    {\"item\": \"Cake5\", \"item_op\": \"5000원\"}"
            + "  ],"
            + "  \"sum\": \"Total\","
            + "  \"sum_op\": \"15000원\""
            + "},"
            + "\"social\": {"
            + "  \"like_count\": 100,"
            + "  \"comment_count\": 200,"
            + "  \"shared_count\": 300,"
            + "  \"view_count\": 400,"
            + "  \"subscriber_count\": 500"
            + "},"
            + "\"buttons\": ["
            + "  {"
            + "    \"title\": \"웹으로 이동\","
            + "    \"link\": {"
            + "      \"web_url\": \"http://www.daum.net\","
            + "      \"mobile_web_url\": \"http://m.daum.net\""
            + "    }"
            + "  },"
            + "  {"
            + "    \"title\": \"앱으로 이동\","
            + "    \"link\": {"
            + "      \"android_execution_params\": \"contentId=100\","
            + "      \"ios_execution_params\": \"contentId=100\""
            + "    }"
            + "  }"
            + "]"
            + "}";
        return templateObject;
    }

    private Long getKakaoId(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("id").asLong();
    }

    private String getKakaoEmail(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("kakao_account").get("email").toString().replaceAll("\"", "");
    }

    public HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    public HttpHeaders getHttpHeadersForSelfMessage(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
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

    public ResponseEntity<String> getHttpSendSelfMessage(MultiValueMap<String, String> body,
        HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(
            body, headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
    }
}
