package gift.web.client;

import gift.authentication.token.KakaoToken;
import gift.web.client.dto.KakaoInfo;
import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoClient")
public interface KakaoClient {

    @PostMapping
    KakaoInfo getKakaoInfo(
        URI uri,
        @RequestHeader("Authorization") String accessToken);

    @PostMapping
    KakaoToken getToken(
        URI uri,
        @RequestParam("code") String code,
        @RequestParam("client_id") String clientId,
        @RequestParam("redirect_uri") String redirectUrl,
        @RequestParam("grant_type") String grantType);
}
