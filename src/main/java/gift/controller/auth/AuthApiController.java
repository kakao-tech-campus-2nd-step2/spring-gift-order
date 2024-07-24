package gift.controller.auth;

import gift.domain.AuthToken;
import gift.dto.request.MemberRequestDto;
import gift.dto.response.MemberResponseDto;
import gift.service.AuthService;
import gift.service.KakaoService;
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
    private final KakaoService kakaoService;

    public AuthApiController(AuthService authService, TokenService tokenService, KakaoService kakaoService) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.kakaoService = kakaoService;
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
    public ResponseEntity<Map<String, String>> renewOAuthToken(HttpServletRequest servletRequest){
        String code = servletRequest.getParameter("code");

        Map<String, String> kakaoTokenInfo = kakaoService.getKakaoOauthToken(code);

        String kakaoUserInformation = kakaoService.getKakaoUserInformation(kakaoTokenInfo.get("access_token"));

        String token = authService.kakaoMemberLogin(kakaoUserInformation, kakaoTokenInfo);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/oauth/renew/kakao")
    public ResponseEntity<Map<String, String>> memberKakaoLogin(HttpServletRequest servletRequest){
        String tokenId = servletRequest.getParameter("token");

        AuthToken findToken = tokenService.findTokenById(Long.valueOf(tokenId));

        Map<String, String> renewTokenInfo = kakaoService.renewToken(findToken);

        AuthToken updateToken = tokenService.updateToken(renewTokenInfo);

        Map<String, String> response = new HashMap<>();
        response.put("token", updateToken.getToken());

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
