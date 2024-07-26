package gift.service;

import gift.dto.KakaoResponseDto;
import gift.model.member.KakaoProperties;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class KakaoService {
    private final KakaoProperties kakaoProperties;
    private final WebClient.Builder webClientBuilder;


    public KakaoService(KakaoProperties kakaoProperties, WebClient.Builder webClientBuilder) {
        this.kakaoProperties = kakaoProperties;
        this.webClientBuilder = webClientBuilder;
    }

    public String getAccessTokenFromKakao(String code) {
        WebClient webClient = webClientBuilder.baseUrl(kakaoProperties.getKakaoAuthUrl()).build();

        KakaoResponseDto kakaoResponseDto = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoProperties.getClientId())
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoResponseDto.class)
                .block();

        return kakaoResponseDto.accessToken();
    }
}