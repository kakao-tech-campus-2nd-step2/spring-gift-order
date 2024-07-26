package gift.domain.order.service;

import gift.domain.member.entity.Member;
import gift.domain.option.entity.Option;
import gift.domain.option.exception.OptionNotFoundException;
import gift.domain.option.repository.OptionRepository;
import gift.domain.option.service.OptionService;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Orders;
import gift.domain.order.repository.OrderRepository;
import gift.domain.wishlist.entity.Wish;
import gift.domain.wishlist.repository.WishRepository;
import gift.kakaoApi.exceptiion.KakaoMessageException;
import gift.kakaoApi.service.KakaoApiService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OrderRepository orderRepository;
    private final KakaoApiService kakaoApiService;
    private final OptionService optionService;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository,
        OrderRepository orderRepository, KakaoApiService kakaoApiService,
        OptionService optionService) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.orderRepository = orderRepository;
        this.kakaoApiService = kakaoApiService;
        this.optionService = optionService;
    }

    @Transactional
    public OrderResponse createOrder(Member member, OrderRequest request) {
        Orders newOrder = dtoToEntity(member, request);
        orderProcess(newOrder);
        return entityToDto(orderRepository.save(newOrder));
    }

    private void orderProcess(Orders order) {

        optionService.subtractQuantity(order.getOption().getId(), order.getQuantity());
        removeIfInWishlist(order.getMember(), order.getOption());

        if (!(kakaoApiService.sendKakaoMessage(order.getMember().getKakaoAccessToken(), order)
            .resultCode()).equals(0)) {
            throw new KakaoMessageException("메세지 전송 실패");
        }
        ;
    }

    private void removeIfInWishlist(Member member, Option option) {
        Optional<Wish> wish = wishRepository.findByProductAndMember(option.getProduct(), member);
        wish.ifPresent(wishRepository::delete);
    }

    private Orders dtoToEntity(Member member, OrderRequest request) {
        Option savedOption = optionRepository.findById(request.getOptionId())
            .orElseThrow(() -> new OptionNotFoundException("해당하는 옵션이 존재하지 않습니다."));
        return new Orders(savedOption, member, request.getQuantity(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
            request.getMessage());
    }

    private OrderResponse entityToDto(Orders order) {
        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage());
    }
}
