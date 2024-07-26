package gift.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.dto.WishlistRequest;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.User;
import gift.entity.Wishlist;
import gift.exception.InvalidOptionException;
import gift.exception.InvalidTokenFormatException;
import gift.exception.InvalidUserException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;

@Service
public class OrderService {

	private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishlistRepository wishlistRepository;
    private final KakaoMessageService kakaoMessageService;
    private final UserService userService;
    private final WishlistService wishlistService;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository,
                        WishlistRepository wishlistRepository, KakaoMessageService kakaoMessageService,
                        UserService userService, WishlistService wishlistService) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishlistRepository = wishlistRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.userService = userService;
        this.wishlistService = wishlistService;
    }

    public OrderResponse createOrder(String token, OrderRequest request, BindingResult bindingResult) {
        validateBindingResult(bindingResult);
        Option option = findOption(request.getOptionId());
        option.decreaseQuantity(request.getQuantity());
        Order order = request.toEntity(option);
        order.setOrderDateTime(LocalDateTime.now());
        orderRepository.save(order);
        User user = userService.getUserFromToken(token);
        removeWishlistOnOrder(user, option.getProduct().getId(), token, bindingResult);

        sendKakaoMessage(token, request.getMessage());
        return order.toDto();
    }

    private Option findOption(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new InvalidOptionException("Option not found."));
    }

    private void sendKakaoMessage(String token, String message) {
        String accessToken = validateExtractToken(token);
        kakaoMessageService.sendMessage(accessToken, message);
    }

    private String validateExtractToken(String token) {
        if (!token.startsWith("Bearer ")) {
            throw new InvalidTokenFormatException("Invalid token format.");
        }
        return token.substring(7);
    }

    private void removeWishlistOnOrder(User user, Long productId, String token, BindingResult bindingResult) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(user.getId(), productId);
        if (wishlist != null) {
        	WishlistRequest request = new WishlistRequest(productId, wishlist.getQuantity());
            wishlistService.removeWishlist(token, request, bindingResult);
        }
    }

    private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
