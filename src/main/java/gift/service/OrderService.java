package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.request.MessageRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.MessageResponse;
import gift.dto.response.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;


@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OptionService optionService;
    private WishListService wishListService;
    private KakaoApiService kakaoApiService;
    private KakaoTokenService kakaoTokenService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishListService wishListService, KakaoTokenService kakaoTokenService){
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishListService = wishListService;
        this.kakaoTokenService = kakaoTokenService;
    }
    
    @Transactional
    public OrderResponse orderOption(OrderRequest orderRequest){

        Option option = optionService.subtractQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());
        Order order = new Order(option, orderRequest.getQuantity(), orderRequest.getMessage(), LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
            savedOrder.getId(), 
            savedOrder.getOption().getId(), 
            savedOrder.getQuantity(), 
            savedOrder.getMessage(), 
            savedOrder.getOrderTime());
    }

    @Transactional
    public void deleteWishListByOrder(String token, Long optionId){

        Product product = optionService.findProductByOptionId(optionId);
        wishListService.deleteWishList(token, product.getId());
    }

    @Transactional
    public void sendKakaoMessage(String token, OrderResponse orderResponse){

        String kakaoToken = kakaoTokenService.findKakaoToken(token);
        
        Product product = optionService.findProductByOptionId(orderResponse.getOptionId());
        MessageRequest messageRequest = new MessageRequest(kakaoToken, orderResponse, product);
        MessageResponse messageResponse = kakaoApiService.sendMessage(messageRequest);
        if(messageResponse.getCode() != 0){
            throw new CustomException("fail to send message", HttpStatus.BAD_REQUEST);
        }
    }


}
