package gift.service;

import gift.config.KakaoProperties;
import gift.dto.KakaoAccessTokenDTO;
import gift.dto.KakaoUserInfoDTO;
import gift.dto.MemberDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();
    private final MemberRepository memberRepository;

    public KakaoService(KakaoProperties kakaoProperties, MemberRepository memberRepository) {
        this.kakaoProperties = kakaoProperties;
        this.memberRepository = memberRepository;
    }

    public String generateKakaoLoginUrl() {
        return kakaoProperties.generateLoginUrl();
    }

    public String getAccessToken(String authorizationCode) {
        String url = kakaoProperties.tokenUrl();
        final LinkedMultiValueMap<String, String> body = createBody(authorizationCode);
        ResponseEntity<KakaoAccessTokenDTO> response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessTokenDTO.class);
        KakaoAccessTokenDTO kakaoAccessTokenDTO = response.getBody();
        return kakaoAccessTokenDTO.accessToken();
    }

    private LinkedMultiValueMap<String, String> createBody(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", authorizationCode);
        return body;
    }

    public String getUserEmail(String accessToken) {
        String url = kakaoProperties.userInfoUrl();
        ResponseEntity<KakaoUserInfoDTO> response = restClient.get()
            .uri(URI.create(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .toEntity(KakaoUserInfoDTO.class);
        KakaoUserInfoDTO kakaoUserInfoDTO = response.getBody();
        return kakaoUserInfoDTO.kakaoAccountDTO().email();
    }

    @Transactional
    public Member saveKakaoUser(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            String name = email.split("@")[0];
            String password = name + name;
            MemberDTO memberDTO = new MemberDTO(name, email, password);
            member = new Member(null, memberDTO.name(), memberDTO.email(), memberDTO.password(), "user");
            memberRepository.save(member);
        }
        return member;
    }
}