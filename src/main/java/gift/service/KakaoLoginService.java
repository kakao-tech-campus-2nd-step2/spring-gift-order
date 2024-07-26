package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.database.repository.JpaMemberRepository;
import gift.dto.LoginMemberToken;
import gift.dto.MemberRequest;
import gift.exceptionAdvisor.exceptions.GiftUnauthorizedException;
import gift.model.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class KakaoLoginService {

    @Value("${kakao.api_key}")
    private String apiKey;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private final JpaMemberRepository jpaMemberRepository;

    private final AuthenticationTool authenticationTool;

    public KakaoLoginService(JpaMemberRepository jpaMemberRepository, AuthenticationTool authenticationTool) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.authenticationTool = authenticationTool;
    }

    public String getConnectionUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
    }

    public String getToken(String code) {
        WebClient webClient = WebClient.create();
        return webClient.post()
            .uri("https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&code=" + code)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public LoginMemberToken getKakaoUser(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try{
            jsonNode = objectMapper.readTree(token);
            Long id = jsonNode.get("id").asLong();
            String email = jsonNode.get("kakao_account").get("email").asText();
            Member member = jpaMemberRepository.findByEmail(email).orElseThrow(() -> new GiftUnauthorizedException("등록되지 않은 회원입니다."));

            return new LoginMemberToken(authenticationTool.makeToken(member));

        } catch (Exception e) {
            throw new GiftUnauthorizedException("카카오 로그인에 실패하였습니다.");
        }

    }
}