package gift.token;

import static gift.exception.ErrorMessage.KAKAO_AUTHENTICATION_FAILED;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import gift.exception.FailedLoginException;
import gift.oauth.KakaoOauthConfigure;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class KakaoJwkProvider {

    private final KakaoOauthConfigure kakaoOauthConfigure;

    public KakaoJwkProvider(KakaoOauthConfigure kakaoOauthConfigure) {
        this.kakaoOauthConfigure = kakaoOauthConfigure;
    }

    private final JwkProvider jwkProvider = new JwkProviderBuilder("https://kauth.kakao.com")
        .cached(10, 7, TimeUnit.DAYS)
        .build();

    private JWTVerifier getVerifierFromToken(String idToken) {
        try {
            Jwk jwk = jwkProvider.get(JWT.decode(idToken).getKeyId());
            return JWT.require(Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null))
                .withIssuer("https://kauth.kakao.com")
                .withAudience(kakaoOauthConfigure.getClientId())
                .acceptLeeway(100)
                .build();
        } catch (JwkException e) {
            throw new FailedLoginException(KAKAO_AUTHENTICATION_FAILED);
        }
    }

    public Pair<String, String> getEmailAndSub(String idToken) {
        Map<String, Claim> claims = getVerifierFromToken(idToken)
            .verify(idToken)
            .getClaims();
        return Pair.of(claims.get("email").asString(), claims.get("sub").asString());
    }
}
