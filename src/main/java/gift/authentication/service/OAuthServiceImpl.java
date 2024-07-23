package gift.authentication.service;

import gift.core.domain.authentication.*;
import gift.core.domain.user.User;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.UserSocialAccount;
import gift.core.domain.user.UserSocialAccountRepository;
import gift.core.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OAuthServiceImpl implements OAuthService {
    private final OAuthGatewayProvider gatewayProvider;
    private final TokenProvider tokenProvider;
    private final UserSocialAccountRepository userSocialAccountRepository;
    private final UserRepository userRepository;

    public OAuthServiceImpl(
            OAuthGatewayProvider gatewayProvider,
            TokenProvider tokenProvider,
            UserSocialAccountRepository userSocialAccountRepository,
            UserRepository userRepository
    ) {
        this.gatewayProvider = gatewayProvider;
        this.userSocialAccountRepository = userSocialAccountRepository;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public Token authenticate(OAuthType type, String token) {
        OAuthGateway gateway = gatewayProvider.getGateway(type);
        OAuthResult result = gateway.authenticate(token);
        User user = findUserBySocialAccountOrCreate(socialAccountOf(type, result.uniqueSocialId()));

        return tokenProvider.generateAccessToken(user.id());
    }

    private User findUserBySocialAccountOrCreate(UserSocialAccount account) {
        User user;
        if (userSocialAccountRepository.exists(account)) {
            Long userId = userSocialAccountRepository.findUserIdBySocialAccount(account);
            user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        } else {
            user = new User(account.provider().name() + " user", account);
            userRepository.save(user);
        }
        return user;
    }

    private static UserSocialAccount socialAccountOf(OAuthType type, String id) {
        return new UserSocialAccount(type, id);
    }
}
