package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestClient restClient;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RestClient restClient) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.restClient = restClient;
    }

    public String register(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(memberRequest.password());
        Member member = new Member(memberRequest.email(), encodedPassword);
        memberRepository.save(member);
        return jwtUtil.generateToken(member.getId(), member.getEmail(), "USER");
    }

    public String authenticate(MemberRequest memberRequest) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberRequest.email());
        if (optionalMember.isPresent() && passwordEncoder.matches(memberRequest.password(), optionalMember.get().getPassword())) {
            Member member = optionalMember.get();
            return jwtUtil.generateToken(member.getId(), member.getEmail(), "USER");
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<WishResponse> wishResponses = member.getWishes().stream()
                .map(wish -> new WishResponse(wish.getId(), wish.getProduct().getId(), wish.getProduct().getName(), wish.getProductNumber()))
                .collect(Collectors.toList());
        return new MemberResponse(member.getId(), member.getEmail(), wishResponses);
    }

    public String kakaoLogin(String code) {
        String clientId = "19376eed1b3349344fe573759afca0a4";
        String redirectUri = "http://localhost:8080/members/oauth/kakao";

        // Request to get access token
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        try {
            String response = restClient
                    .post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .body(body)
                    .retrieve()
                    .body(String.class);

            // 디버깅을 위한 응답 출력
            // System.out.println("Token Response: " + response);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> tokenResponse = objectMapper.readValue(response, Map.class);
            String accessToken = (String) tokenResponse.get("access_token");

            if (accessToken == null) {
                throw new RuntimeException("Failed to retrieve access token");
            }

            String userInfoResponse = restClient
                    .get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            // 디버깅을 위한 응답 출력
            // System.out.println("User Info Response: " + userInfoResponse);

            Map<String, Object> userInfo = objectMapper.readValue(userInfoResponse, Map.class);
            Long kakaoId = (Long) userInfo.get("id");
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String nickname = null;
            if (profile != null) {
                nickname = (String) profile.get("nickname");
            }

            if (nickname == null) {
                throw new RuntimeException("Failed to retrieve user nickname");
            }

            // 임의의 이메일 생성
            String email = "kakao_" + kakaoId + "_" + nickname.replaceAll("[^a-zA-Z0-9]", "_") + "@kakao.com";

            Optional<Member> optionalMember = memberRepository.findByEmail(email);
            Member member;
            if (optionalMember.isPresent()) {
                member = optionalMember.get();
            } else {
                member = new Member(email, passwordEncoder.encode(UUID.randomUUID().toString()));
                memberRepository.save(member);
            }

            return jwtUtil.generateToken(member.getId(), member.getEmail(), "USER");
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve Kakao token", e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse Kakao response", e);
        }
    }
}
