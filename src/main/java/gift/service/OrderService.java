package gift.service;

import gift.config.WebClientUtil;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.exception.ValueNotFoundException;
import gift.model.member.KakaoProperties;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.wish.Wish;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
    private final WebClientUtil webClientUtil;
    private final KakaoProperties kakaoProperties;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private WishRepository wishRepository;

    public OrderService(KakaoProperties kakaoProperties,WebClient.Builder webClientBuilder){
        this.kakaoProperties = kakaoProperties;
        this.webClientUtil = new WebClientUtil(webClientBuilder);
    }

    public OrderResponseDto requestOrder(OrderRequestDto orderRequestDto, String accessToken){
        Option option = optionRepository.findById(orderRequestDto.optionId()).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));
        if(option.isProductNotEnough(orderRequestDto.quantity())){
            throw new RuntimeException("Not enough product available");
        }
        option.updateAmount(orderRequestDto.quantity());
        optionRepository.save(option);

        Product product = option.getProduct();
        Optional<Wish> wish = wishRepository.findByProductId(product.getId());
        if(wish.isPresent()){
            wishRepository.delete(wish.get());
        }

        int resultCode = sendOrderConfirmationMessage(orderRequestDto,accessToken);
        if(resultCode != 0){
            throw new RuntimeException("Kakao Server Error");
        }
        OrderResponseDto responseDto = new OrderResponseDto(
                1,
                orderRequestDto.optionId(),
                orderRequestDto.quantity(),
                LocalDateTime.now(),
                orderRequestDto.message()
        );

        return responseDto;
    }
    public int sendOrderConfirmationMessage(OrderRequestDto orderRequestDto, String accessToken){
        WebClient webClient = webClientUtil.createWebClient(kakaoProperties.getUserInfoUrl());

        WebClient.ResponseSpec responseSpec = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/api/talk/memo/default/send")
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("template_object=" + orderRequestDto.message())
                .retrieve();

        webClientUtil.handleErrorResponses(responseSpec);
        return responseSpec.bodyToMono(Integer.class).block();
    }
}
