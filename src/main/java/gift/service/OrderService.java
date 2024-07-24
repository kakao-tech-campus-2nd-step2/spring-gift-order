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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final KakaoApiClient kakaoApiClient;
    private final TokenService tokenService;

    public OrderService(OrderRepository orderRepository, ProductOptionRepository productOptionRepository,
                        UserRepository userRepository, WishRepository wishRepository,
                        KakaoApiClient kakaoApiClient, TokenService tokenService) {
        this.orderRepository = orderRepository;
        this.productOptionRepository = productOptionRepository;
        this.userRepository = userRepository;
        this.wishRepository = wishRepository;
        this.kakaoApiClient = kakaoApiClient;
        this.tokenService = tokenService;
    }

    @Transactional
    public OrderResponseDto createOrder(String jwtToken, String kakaoAccessToken, OrderRequestDto requestDto) {
        String jwt = jwtToken.replace("Bearer ", "");
        String kakaoToken = kakaoAccessToken.replace("Bearer ", "");

        Map<String, String> userInfo = tokenService.extractUserInfo(jwt);
        String userId = userInfo.get("id");

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ProductOption productOption = productOptionRepository.findById(requestDto.getProductOptionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));

        if (productOption.getQuantity() < requestDto.getQuantity()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_QUANTITY);
        }

        productOption.decreaseQuantity(requestDto.getQuantity());
        productOptionRepository.save(productOption);

        Order order = new Order(productOption, user, requestDto.getQuantity(), LocalDateTime.now(), requestDto.getMessage());
        orderRepository.save(order);

        wishRepository.deleteByUserAndProduct(user, productOption.getProduct());

        sendOrderConfirmationMessage(kakaoToken, order);

        return new OrderResponseDto(order.getId(), productOption.getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }

    private void sendOrderConfirmationMessage(String kakaoAccessToken, Order order) {
        kakaoApiClient.sendMessageToMe(kakaoAccessToken, order);
    }
}
