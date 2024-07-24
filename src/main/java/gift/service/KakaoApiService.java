package gift.service;

import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.KakaoApiDTO.KakaoOrderResponse;
import gift.controller.dto.KakaoTokenDto;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Token;
import gift.domain.UserInfo;
import gift.domain.Wish;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.TokenRepository;
import gift.repository.UserInfoRepository;
import gift.repository.WishRepository;
import gift.utils.ExternalApiService;
import gift.utils.config.KakaoProperties;
import gift.utils.error.KakaoLoginException;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {
    private final ExternalApiService externalApiService;
    private final KakaoProperties kakaoProperties;
    private final OptionRepository optionRepository;
    private final UserInfoRepository userInfoRepository;
    private final TokenRepository tokenRepository;
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;

    public KakaoApiService(ExternalApiService externalApiService, KakaoProperties kakaoProperties,
        OptionRepository optionRepository, UserInfoRepository userInfoRepository,
        TokenRepository tokenRepository, OrderRepository orderRepository,
        WishRepository wishRepository) {
        this.externalApiService = externalApiService;
        this.kakaoProperties = kakaoProperties;
        this.optionRepository = optionRepository;
        this.userInfoRepository = userInfoRepository;
        this.tokenRepository = tokenRepository;
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
    }


    public String createKakaoCode(){
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize";

        return UriComponentsBuilder.fromHttpUrl(kakaoAuthUrl)
            .queryParam("client_id", kakaoProperties.getRestApiKey())
            .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
            .queryParam("response_type", "code")
            .toUriString();
    }
    @Transactional
    public String createKakaoToken(String code,
        String error,
        String error_description,
        String state){
        if (code==null && error!=null){
            throw new KakaoLoginException(error_description);
        }
        ResponseEntity<KakaoTokenDto> kakaoTokenDtoResponseEntity = externalApiService.postKakaoToken(
            code);
        String s = externalApiService.postKakaoUserId(
            kakaoTokenDtoResponseEntity.getBody().getAccessToken());

        String email = s+"@kakao.com";

        Optional<UserInfo> byEmail = userInfoRepository.findByEmail(email);
        if (byEmail.isEmpty()){
            UserInfo userInfo = new UserInfo(email, email);
            userInfoRepository.save(userInfo);
        }
        Token token = new Token(email, kakaoTokenDtoResponseEntity.getBody().getAccessToken());
        tokenRepository.save(token);

        return kakaoTokenDtoResponseEntity.getBody().getAccessToken();

    }

    @Transactional
    public KakaoApiDTO.KakaoOrderResponse kakaoOrder(KakaoApiDTO.KakaoOrderRequest kakaoOrderRequest,String accessToken){
        Option option = optionRepository.findById(kakaoOrderRequest.optionId()).orElseThrow(
            () -> new OptionNotFoundException("Option Not Found")
        );

        Token byToken = tokenRepository.findByToken(accessToken);
        String email = byToken.getEmail();

        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );


        Order order = new Order(kakaoOrderRequest.quantity(),
            LocalDateTime.now(), kakaoOrderRequest.message());

        userInfo.addOrder(order);
        option.addOrder(order);

        Optional<Wish> byUserInfoIdAndProductId = wishRepository.findByUserInfoIdAndProductId(
            userInfo.getId(), option.getProduct().getId());

        byUserInfoIdAndProductId.ifPresent(wishRepository::delete);

        option.subtract(kakaoOrderRequest.quantity());

        orderRepository.save(order);

        KakaoOrderResponse kakaoOrderResponse = new KakaoOrderResponse(order.getId(),
            option.getId(), order.getQuantity(), order.getOrderDateTime(),
            order.getMessage());

        externalApiService.postSendMe(kakaoOrderResponse,accessToken);

        return kakaoOrderResponse;

    }


}
