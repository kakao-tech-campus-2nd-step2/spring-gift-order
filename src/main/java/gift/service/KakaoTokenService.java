package gift.service;

import gift.dto.KakaoUserInfoDTO;
import gift.dto.MemberDTO;
import gift.dto.TokenResponseDTO;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.InternalServerExceptions.InternalServerException;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoTokenService {
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestClient client = RestClient.builder().build();

    @Value("${kakao-redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao-rest-api-key}")
    private String clientId;

    public String getAccessToken(String code){
        try {
            var body = makeBody(clientId, kakaoRedirectUri, code);
            ResponseEntity<TokenResponseDTO> result = client.post()
                    .uri(URI.create(TOKEN_URL)).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body).retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new BadRequestException("잘못된 요청으로 인한 오류입니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new BadRequestException("서버에서 오류가 발생하였습니다.\n" + response.getBody()
                        .toString().replace("{", "").replace("}", "").trim()
                        );
                    }).toEntity(TokenResponseDTO.class);

            return Objects.requireNonNull(result.getBody()).accessToken();
        } catch (BadRequestException | InternalServerException e){
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public MemberDTO getUserInfo(String accessToken){
        try {
            ResponseEntity<KakaoUserInfoDTO> responseUserInfo = client.get()
                    .uri(URI.create(USER_INFO_URL))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new BadRequestException("잘못된 요청으로 인한 오류입니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new BadRequestException("서버에서 오류가 발생하였습니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    }).toEntity(KakaoUserInfoDTO.class);

            KakaoUserInfoDTO userInfo = responseUserInfo.getBody();
            KakaoUserInfoDTO.KakaoAccount kakaoAccount = Objects.requireNonNull(userInfo).kakaoAccount();
            KakaoUserInfoDTO.KakaoAccount.Profile profile = kakaoAccount.profile();

            return new MemberDTO(kakaoAccount.email(), "default", "social",  profile.nickname());
        } catch (BadRequestException | InternalServerException e){
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }


    private LinkedMultiValueMap<String, String> makeBody(String clientId, String kakaoRedirectUri, String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", kakaoRedirectUri);
        body.add("code", code);
        return body;
    }

}
