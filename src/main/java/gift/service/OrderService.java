package gift.service;

import gift.domain.*;
import gift.dto.request.OrderRequestDto;
import gift.dto.response.OrderResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.repository.member.MemberRepository;
import gift.repository.option.OptionRepository;
import gift.repository.order.OrderRepository;
import gift.repository.wish.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static gift.exception.exceptionMessage.ExceptionMessage.MEMBER_NOT_FOUND;
import static gift.exception.exceptionMessage.ExceptionMessage.OPTION_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;

    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository,
                        OptionRepository optionRepository,
                        WishRepository wishRepository,
                        MemberRepository memberRepository,
                        KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public OrderResponseDto addOrder(OrderRequestDto orderRequestDto, AuthToken token) {
        Option findOption = optionRepository.findOptionByIdForUpdate(orderRequestDto.optionId())
                .orElseThrow(() -> new EntityNotFoundException(OPTION_NOT_FOUND));

        Member findMember = memberRepository.findMemberByEmail(token.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

        findOption.updateQuantity(orderRequestDto.quantity());

        Optional<Wish> findWish = wishRepository.findWishByProductIdAndMemberEmail(findOption.getProduct().getId(), token.getEmail());
        findWish.ifPresent(wishRepository::delete);

        Order order = new Order.Builder()
                .option(findOption)
                .member(findMember)
                .quantity(orderRequestDto.quantity())
                .message(orderRequestDto.message())
                .build();

        Order savedOrder = orderRepository.save(order);

        OrderResponseDto orderResponseDto = OrderResponseDto.from(savedOrder);

        if (token.getAccessToken() != null) {
            kakaoService.sendKakaoMessage(token.getAccessToken(), orderResponseDto);
        }

        return orderResponseDto;
    }
}
