package gift.service;

import gift.config.WebClientUtil;
import gift.dto.KakaoInfoDto;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.MemberDto;
import gift.exception.ValueAlreadyExistsException;
import gift.model.member.KakaoProperties;
import gift.model.member.Member;
import gift.repository.MemberRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class KakaoService {
    @Autowired
    private KakaoProperties kakaoProperties;
    @Autowired
    private WebClientUtil webClientUtil;
    @Autowired
    private MemberRepository memberRepository;

    public String getAccessTokenFromKakao(String code) {
        WebClient webClient = webClientUtil.createWebClient(kakaoProperties.getKakaoAuthUrl());

        WebClient.ResponseSpec responseSpec = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoProperties.getClientId())
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve();

        webClientUtil.handleErrorResponses(responseSpec);
        KakaoTokenResponseDto kakaoTokenResponseDto = responseSpec.bodyToMono(KakaoTokenResponseDto.class).block();

        return kakaoTokenResponseDto.accessToken();
    }

    public KakaoInfoDto getUserInfo(String accessToken){
        WebClient webClient = webClientUtil.createWebClient(kakaoProperties.getUserInfoUrl());

        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve();

        webClientUtil.handleErrorResponses(responseSpec);

        KakaoInfoDto kakaoInfoDto = responseSpec.bodyToMono(KakaoInfoDto.class).block();
        return kakaoInfoDto;
    }
    public Member registerOrGetKakaoMember(String email){
        Optional<Member> kakaoMember = memberRepository.findByEmail(email);
        if(kakaoMember.isEmpty()){
            String tempPassword = new SecureRandom().toString();
            Member newKakaoMember = new Member(email,tempPassword,"member");
            return memberRepository.save(newKakaoMember);
        }
        return kakaoMember.get();
    }

    public void kakaoDisconnect(String accessToken){
        WebClient webClient = webClientUtil.createWebClient(kakaoProperties.getUserInfoUrl());

        WebClient.ResponseSpec responseSpec = webClient.post()
                .uri("/v1/user/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve();
        webClientUtil.handleErrorResponses(responseSpec);
    }
}