package gift.api.member;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import gift.api.member.dto.KakaoAccount;
import gift.api.member.dto.MemberRequest;
import gift.api.member.dto.TokenResponse;
import gift.api.member.dto.UserInfoResponse;
import gift.api.member.exception.EmailAgreementNeededException;
import gift.api.member.exception.EmailAlreadyExistsException;
import gift.api.member.exception.RegisterNeededException;
import gift.global.config.KakaoProperties;
import gift.global.exception.ForbiddenMemberException;
import gift.global.exception.UnauthorizedMemberException;
import gift.global.utils.JwtUtil;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final MemberRepository memberRepository;
    private final KakaoProperties properties;
    private final RestClient restClient;

    public MemberService(MemberDao memberDao, MemberRepository memberRepository, KakaoProperties properties) {
        this.memberDao = memberDao;
        this.memberRepository = memberRepository;
        this.properties = properties;
        restClient = RestClient.create();
    }

    @Transactional
    public Long register(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.email())) {
            throw new EmailAlreadyExistsException();
        }
        return memberRepository.save(memberRequest.toEntity()).getId();
    }

    public void login(MemberRequest memberRequest, String token) {
        if (memberRepository.existsByEmailAndPassword(memberRequest.email(), memberRequest.password())) {
            Long id = memberDao.findMemberByEmail(memberRequest.email()).getId();
            if (token.equals(JwtUtil.generateAccessToken(id, memberRequest.email(), memberRequest.role()))) {
                return;
            }
            throw new UnauthorizedMemberException();
        }
        throw new ForbiddenMemberException();
    }

    public ResponseEntity<TokenResponse> obtainToken(String code) {
        return restClient.post()
            .uri(URI.create(properties.url().token()))
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(createBody(code))
            .retrieve()
            .toEntity(TokenResponse.class);
    }

    public ResponseEntity<UserInfoResponse> obtainUserInfo(ResponseEntity<TokenResponse> tokenResponse) {
        return restClient.get()
            .uri(properties.url().user(), uriBuilder -> uriBuilder
                .queryParam("property_keys", "[\"kakao_account.email\"]")
                .build())
            .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .header(AUTHORIZATION, "Bearer " + tokenResponse.getBody().accessToken())
            .retrieve()
            .toEntity(UserInfoResponse.class);
    }

    public void loginKakao(ResponseEntity<UserInfoResponse> userInfoResponse) {
        KakaoAccount kakaoAccount = userInfoResponse.getBody().kakaoAccount();
        if (kakaoAccount.emailNeedsAgreement()) {
            throw new EmailAgreementNeededException();
        }
        if (kakaoAccount.isEmailValid()) {
            if (!memberRepository.existsByEmail(kakaoAccount.email())) {
                throw new RegisterNeededException();
            }
        }
    }

    @Transactional
    public void saveKakaoToken(ResponseEntity<TokenResponse> tokenResponse, ResponseEntity<UserInfoResponse> userInfoResponse) {
        Member member = memberDao.findMemberByEmail(userInfoResponse.getBody().kakaoAccount().email());
        member.saveKakaoToken(tokenResponse.getBody().accessToken());
    }

    public LinkedMultiValueMap<Object, Object> createBody(String code) {
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.url().redirect());
        body.add("code", code);
        return body;
    }
}
