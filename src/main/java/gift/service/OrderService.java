package gift.service;

import gift.client.KakaoApiClient;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Order;
import gift.entity.ProductOption;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {
    private final ProductOptionRepository productOptionRepository;
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final KakaoApiClient kakaoApiClient;

    public OrderService(ProductOptionRepository productOptionRepository, OrderRepository orderRepository, WishRepository wishRepository, UserRepository userRepository, TokenService tokenService, KakaoApiClient kakaoApiClient) {
        this.productOptionRepository = productOptionRepository;
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.kakaoApiClient = kakaoApiClient;
    }

    @Transactional
    public OrderResponseDto createOrder(String jwtToken, String kakaoAccessToken, OrderRequestDto requestDto) {
        Map<String, String> userInfo = tokenService.extractUserInfo(jwtToken);
        Long userId = Long.parseLong(userInfo.get("id"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ProductOption productOption = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));

        productOption.decreaseQuantity(requestDto.getQuantity());
        productOptionRepository.save(productOption);

        Order order = new Order(productOption, user, requestDto.getQuantity(), LocalDateTime.now(), requestDto.getMessage());
        Order savedOrder = orderRepository.save(order);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE); // 모든 위시 항목을 가져오기 위해 설정
        wishRepository.findByUser(user, pageable).stream()
                .filter(wish -> wish.getProduct().equals(productOption.getProduct()))
                .forEach(wishRepository::delete);

        String message = String.format("%s 님이 %s를 %d만큼 주문했습니다.\n%s",
                user.getKakaoUser().getNickname(), productOption.getProduct().getName().getValue(), requestDto.getQuantity(), requestDto.getMessage());

        kakaoApiClient.sendKakaoMessage(kakaoAccessToken, message);

        return new OrderResponseDto(savedOrder.getId(), productOption.getId(), requestDto.getQuantity(), savedOrder.getOrderDateTime(), requestDto.getMessage());
    }
}
