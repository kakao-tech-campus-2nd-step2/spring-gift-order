package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.dto.KakaoToken;
import gift.dto.TokenResponseDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.util.JwtUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class KakaoService {
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO = "https://kapi.kakao.com/v2/user/me";
    private final MemberRepository memberRepository;
    private final KakaoProperties kakaoProperties;
    private ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public KakaoService(MemberRepository memberRepository, KakaoProperties kakaoProperties, ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }


    //TODO
    //토큰 파싱하고 유효성 검사하고 사용자 정보 조회
    //1. 사용자 정보 확인
    //2. (신규회원일경우) 가입 처리
    //3. 로그인 완료

    public KakaoToken getKakaoToken(String code) {
        //code 넣고 토큰 받아오기
        RestClient client = RestClient.builder().build();
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectURL());
        body.add("code", code);

        var response = client.post()
                .uri(URI.create(KAKAO_TOKEN_URI))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println(response);
            throw new CustomException(ErrorCode.UNAUTHORIZED_KAKAO);
        }
        return parseToken(response.getBody());
        //<200 OK OK,{"access_token":"eJ7Tu-TAR8JW4SXoOH6DLyp1UZfhVqC6AAAAAQo8JB8AAAGQ6ucoXLZbzBbpXusm","token_type":"bearer","refresh_token":"xA9fCgg9ClNRHsmjK11PFYVHSd0NpwDeAAAAAgo8JB8AAAGQ6ucoWbZbzBbpXusm","expires_in":21599,"scope":"talk_message","refresh_token_expires_in":5183999},
    }

    private KakaoToken parseToken(String response) {
        objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            String accessToken = rootNode.path("access_token").asText();
            String refreshToken = rootNode.path("refresh_token").asText();
            return new KakaoToken(accessToken,refreshToken);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("토큰을 처리하는 중 오류가 발생하였습니다.");
        }
    }

    public TokenResponseDto generateToken(String accessToken) {
        //TODO 유저 정보 조회 후
        RestTemplate client = new RestTemplateBuilder().build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+accessToken);
        headers.set("Content-type",MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var request = new RequestEntity<>(headers, HttpMethod.POST, URI.create(KAKAO_USER_INFO));
        var response = client.exchange(request, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("사용자 정보 조회 오류");
        }
        Member member = registerMember(response.getBody());
        String token = jwtUtil.generateToken(member.getId(), member.getEmail());
        return new TokenResponseDto(token);
    }

    private Member registerMember(String body) {
        Map<String, String> memberInfo = new HashMap<>();
        try {
            JsonNode rootNode = objectMapper.readTree(body);
            memberInfo.put("email", rootNode.path("email").asText());
            memberInfo.put("name", rootNode.path("name").asText());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("사용자 정보를 가져오는 과정에서 오류가 발생하였습니다");
        }
        Optional<Member> member = memberRepository.findByEmail(memberInfo.get("email"));
        if (member.isEmpty()) {
            Member signupMember = new Member(memberInfo.get("email"), memberInfo.get("name"), UUID.randomUUID().toString(),1);
            memberRepository.save(signupMember);
            return signupMember;
        }
        return member.get();
    }
}


