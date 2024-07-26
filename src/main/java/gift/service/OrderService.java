package gift.service;

import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.exception.option.OptionNotFoundException;
import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.service.oauth.KakaoOAuthService;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoOAuthService kakaoOAuthService;
    private final WishService wishService;

    public OrderService(
        OrderRepository orderRepository,
        OptionRepository optionRepository,
        KakaoOAuthService kakaoOAuthService,
        WishService wishService
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoOAuthService = kakaoOAuthService;
        this.wishService = wishService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Long memberId) {
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionNotFoundException(OPTION_NOT_FOUND + orderRequest.optionId()));

        option.subtractQuantity(orderRequest.quantity());
        optionRepository.save(option);

        Order order = new Order(option, orderRequest.quantity(), orderRequest.message(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        sendKakaoMessage(savedOrder, memberId);
        wishService.deleteWishByProductIdAndMemberId(option.getProduct().getId(), memberId);

        return new OrderResponse(
            savedOrder.getId(),
            option.getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

    private void sendKakaoMessage(Order order, Long memberId) {
        String message = createKakaoMessage(order);
        kakaoOAuthService.sendMessage(memberId, message);
    }

    private String createKakaoMessage(Order order) {
        Option option = order.getOption();
        Product product = option.getProduct();

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        String priceFormatted = numberFormat.format(product.getPrice());
        String quantityFormatted = numberFormat.format(order.getQuantity());
        String totalFormatted = numberFormat.format((long) product.getPrice() * order.getQuantity());

        String template = """
            {
                "object_type": "feed",
                "content": {
                    "title": "주문 완료 | 옵션: [%s] %s",
                    "description": "주문 메시지: %s",
                    "image_url": "https://picsum.photos/400/400",
                    "link": {
                        "web_url": "http://www.daum.net",
                        "mobile_web_url": "http://m.daum.net"
                    }
                },
                "item_content": {
                    "profile_text": "충남대 BE 이경빈",
                    "profile_image_url" :"https://avatars.githubusercontent.com/u/109949453?v=4",
                    "title_image_url": "%s",
                    "title_image_text": "%s",
                    "title_image_category": "%s",
                    "items": [
                        {
                            "item": "가격",
                            "item_op": "%s원"
                        },
                        {
                            "item": "수량",
                            "item_op": "%s개"
                        }
                    ],
                    "sum": "Total",
                    "sum_op": "%s원"
                },
                "social": {
                    "like_count": 100,
                    "comment_count": 200,
                    "shared_count": 300,
                    "view_count": 400,
                    "subscriber_count": 500
                }
            }
            """;

        return String.format(template,
            option.getId().toString(), option.getName(), order.getMessage(),
            product.getImageUrl(), product.getName(), product.getCategoryName(),
            priceFormatted, quantityFormatted, totalFormatted
        );
    }
}
