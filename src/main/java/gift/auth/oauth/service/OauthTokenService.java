package gift.auth.oauth.service;

import gift.auth.oauth.entity.OauthToken;
import gift.auth.oauth.repository.OauthTokenJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OauthTokenService {

    private final OauthTokenJpaRepository oauthTokenJpaRepository;

    public OauthTokenService(OauthTokenJpaRepository oauthTokenJpaRepository) {
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
    }

    public void save(OauthToken oauthToken) {
        oauthTokenJpaRepository.save(oauthToken);
    }
}
