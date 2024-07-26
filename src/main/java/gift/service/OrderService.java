package gift.service;

import org.springframework.stereotype.Service;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.repository.OrderRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;


@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OptionService optionService;
    private WishListService wishListService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishListService wishListService){
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishListService = wishListService;
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

    public void deleteWishListByOrder(String token, Long optionId){

        Product product = optionService.findProductByOptionId(optionId);

        wishListService.deleteWishList(token, product.getId());
    }
}
