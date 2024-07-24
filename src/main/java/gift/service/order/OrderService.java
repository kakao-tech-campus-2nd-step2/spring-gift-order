package gift.service.order;

import gift.dto.gift.GiftResponse;
import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.exception.WishItemNotFoundException;
import gift.model.gift.Gift;
import gift.model.option.Option;
import gift.model.order.Order;
import gift.model.user.User;
import gift.model.wish.Wish;
import gift.repository.gift.GiftRepository;
import gift.repository.option.OptionRepository;
import gift.repository.order.OrderRepository;
import gift.repository.user.UserRepository;
import gift.repository.wish.WishRepository;
import gift.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private OptionRepository optionRepository;

    private GiftRepository giftRepository;

    private WishRepository wishRepository;

    private UserRepository userRepository;

    private final AuthUtil authUtil;

    @Autowired
    public OrderService(OptionRepository optionRepository, GiftRepository giftRepository, WishRepository wishRepository, UserRepository userRepository, OrderRepository orderRepository, AuthUtil authUtil) {
        this.optionRepository = optionRepository;
        this.giftRepository = giftRepository;
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.authUtil = authUtil;
    }


    public OrderResponse order(Long userId, Long giftId, OrderRequest.Create orderRequest,String accessToken){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Gift gift = giftRepository.findById(giftId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + giftId));
        Option option = optionRepository.findById(orderRequest.optionId())
                .orElseThrow(() -> new NoSuchElementException("해당 옵션을 찾을 수 없습니다 id :  " + orderRequest.optionId()));

        checkOptionInGift(gift, orderRequest.optionId());
        option.subtract(orderRequest.quantity());
        optionRepository.save(option);

        wishRepository.findByUserAndGift(user,gift)
                .ifPresent(wish -> wishRepository.deleteById(wish.getId()));

        Order order = new Order(option.getId(), orderRequest.quantity(), orderRequest.message());
        orderRepository.save(order);
        authUtil.sendMessage(accessToken, orderRequest.message());
        return OrderResponse.fromEntity(order);
    }

    public void checkOptionInGift(Gift gift, Long optionId) {
        if (!gift.hasOption(optionId)) {
            throw new NoSuchElementException("해당 상품에 해당 옵션이 없습니다!");
        }
    }
}
