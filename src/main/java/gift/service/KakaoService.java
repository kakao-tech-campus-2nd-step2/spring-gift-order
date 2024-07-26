package gift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoService {
    @Value("${my.client_id}")
    private String client_id;

    public URI makeUri() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", "http://localhost:8080/api/kakao/code")
                .queryParam("response_type", "code")
                .encode()
                .build()
                .toUri();
        System.out.println(uri);
        return uri;
    }
}
