package gift.service;

import gift.dto.OrderDTO;
import gift.model.Option;
import gift.model.User;
import gift.repository.OptionRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    private final OptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final KakaoService kakaoService;

    @Autowired
    public OrderService(OptionRepository optionRepository, WishlistRepository wishlistRepository, KakaoService kakaoService) {
        this.optionRepository = optionRepository;
        this.wishlistRepository = wishlistRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, User user) {
        try {
            LOGGER.info("Fetching option with ID: " + orderDTO.getOptionId());
            Option option = optionRepository.findById(orderDTO.getOptionId())
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 옵션 ID입니다: " + orderDTO.getOptionId()));

            LOGGER.info("Fetched option: " + option.getName() + " with quantity: " + option.getQuantity());

            // 상품 옵션의 수량 차감
            option.subtractQuantity(orderDTO.getQuantity());
            LOGGER.info("Updated option quantity: " + option.getQuantity());
            optionRepository.save(option);

            // 위시 리스트에서 삭제
            LOGGER.info("Deleting option from wishlist for user: " + user.getEmail());
            wishlistRepository.deleteByUserEmailAndProductId(user.getEmail(), option.getProduct().getId());

            // 주문 내역 메시지 전송
            String message = orderDTO.getMessage() + "\n옵션: " + option.getName() + "\n수량: " + orderDTO.getQuantity();
            LOGGER.info("Sending message to user: " + user.getEmail());
            kakaoService.sendMessageToMe(user.getEmail(), message);

            // 주문 내역 생성
            orderDTO.setOrderDateTime(LocalDateTime.now());
            orderDTO.setId(System.currentTimeMillis());  // 임시 ID 생성

            LOGGER.info("Order created with ID: " + orderDTO.getId());

            return orderDTO;
        } catch (Exception e) {
            LOGGER.severe("주문 생성 실패: " + e.getMessage());
            e.printStackTrace();  // 예외 로깅
            throw new RuntimeException("주문 생성 실패", e);  // 예외 다시 던지기
        }
    }
}