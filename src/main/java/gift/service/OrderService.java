package gift.service;

import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.User.UserResponse;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.WishProduct;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishListRepository wishListRepository;

    public OrderService(
            OrderRepository orderRepository, OptionRepository optionRepository,
            WishListRepository wishListRepository
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishListRepository = wishListRepository;
    }
    /*
     * 주문 정보를 저장하는 로직
     */
    @Transactional
    public OrderResponse save(OrderRequest orderRequest){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String orderDateTime = now.format(formatter);
        Order order = new Order(
                orderRequest.getOptionId(),
                orderRequest.getQuantity(),
                orderRequest.getMessage(),
                orderDateTime
        );

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }
    /*
     * 주문 전반을 진행하는 로직
     * 수량 감소 및 위시 리스트에서 삭제 -> 메세지 전송 -> 저장 -> 주문 정보 전달
     */
    @Transactional
    public OrderResponse order(OrderRequest orderRequest, UserResponse userResponse){
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(NoSuchFieldError::new);
        Long before = option.getQuantity();
        option.subtract(orderRequest.getQuantity());
        Long after = option.getQuantity();

        if(before.equals(after)){
            return new OrderResponse();
        }

        List<WishProduct> wishList = wishListRepository.findByUserId(userResponse.getId());
        for (WishProduct wishProduct : wishList) {
            List<Option> options = wishProduct.getProduct().getOptions();
            if(options.contains(option)){
                wishListRepository.deleteById(wishProduct.getId());
                break;
            }
        }

        return save(orderRequest);
    }
}
