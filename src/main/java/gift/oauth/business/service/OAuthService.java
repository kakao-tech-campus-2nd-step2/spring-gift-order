package gift.oauth.business.service;

import gift.oauth.business.client.OAuthApiClient;
import gift.oauth.business.dto.OAuthParam;
import gift.oauth.business.dto.OAuthProvider;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {
    private final Map<OAuthProvider, OAuthApiClient> clients;

    public OAuthService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
            Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity()));
    }

    public void getAccessToken(OAuthParam param) {
        var client = clients.get(param.oAuthProvider());
        client.getAccessToken(param);
    }
}
