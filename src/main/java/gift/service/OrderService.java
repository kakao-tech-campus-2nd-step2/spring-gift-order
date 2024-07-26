package gift.service;

import gift.domain.OrderDTO;
import gift.entity.OptionEntity;
import gift.entity.OrderEntity;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OptionService optionService;
    private final OrderRepository orderRepository;
    private final WishListService wishListService;
//    private final KakaoApiService kakaoApiService;

    @Autowired
    public OrderService(OptionService optionService, OrderRepository orderRepository,
        WishListService wishListService) {
        this.optionService = optionService;
        this.orderRepository = orderRepository;
        this.wishListService = wishListService;
//        this.kakaoApiService = kakaoApiService;
    }

    @Transactional
    public void createOrder(OrderDTO order, Long userId, String email) {
        Long product_id = optionService.getProductIdFromOption(order.getOptionId());
        OptionEntity option = optionService.readProductOption(product_id, order.getOptionId());


        if (option.getQuantity() < order.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        optionService.subtractOptionQuantity(order.getOptionId(), order.getQuantity());

        wishListService.removeOptionFromWishList(userId, order.getOptionId());

        OrderEntity orderEntity = new OrderEntity(
            order.getOptionId(),
            order.getQuantity(),
            LocalDateTime.now().toString(),
            order.getMessage()
        );

        orderRepository.save(orderEntity);

//        kakaoApiService.sendOrderMessage(email,orderEntity);
    }
}
