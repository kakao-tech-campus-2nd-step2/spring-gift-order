package gift.controller.auth;

import gift.dto.request.MemberRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.service.AuthService;
import gift.service.KakaoOauthService;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthApiController {

    private final AuthService authService;
    private final TokenService tokenService;

    private final KakaoOauthService kakaoOauthService;

    public AuthApiController(AuthService authService, TokenService tokenService, KakaoOauthService kakaoOauthService) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.kakaoOauthService = kakaoOauthService;
    }

    @PostMapping("/members/register")
    public ResponseEntity<Map<String, String>> memberSignUp(@RequestBody @Valid MemberRequestDto memberRequestDto){
        authService.memberJoin(memberRequestDto);

        Map<String, String> response = getToken(memberRequestDto.email());

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping("/members/login")
    public ResponseEntity<Map<String, String>> memberLogin(@RequestBody @Valid MemberRequestDto memberRequestDto){
        MemberResponseDto memberResponseDto = authService.findOneByEmailAndPassword(memberRequestDto);

        Map<String, String> response = getToken(memberResponseDto.email());

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/members/login/oauth/kakao")
    public ResponseEntity<Map<String, String>> memberKakaoLogin(HttpServletRequest servletRequest){
        String code = servletRequest.getParameter("code");

        String kakaoAccessToken = kakaoOauthService.getKakaoAccessToken(code);

        String kakaoUserInformation = kakaoOauthService.getKakaoUserInformation(kakaoAccessToken);

        MemberResponseDto memberResponseDto = authService.kakaoMemberLogin(kakaoUserInformation);

        tokenService.tokenSave(kakaoAccessToken, memberResponseDto.email());

        Map<String, String> response = new HashMap<>();
        response.put("token", kakaoAccessToken);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }


    public Map<String, String> getToken(String memberRequestDto) {
        UUID uuid = UUID.randomUUID();
        tokenService.tokenSave(uuid.toString(), memberRequestDto);

        Map<String, String> response = new HashMap<>();
        response.put("token", uuid.toString());
        return response;
    }

}
