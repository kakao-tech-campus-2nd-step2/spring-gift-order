package gift.order;

import gift.auth.KakaoClient;
import gift.exception.NotFoundMember;
import gift.exception.NotFoundOption;
import gift.exception.NotFoundOrder;
import gift.member.Member;
import gift.member.MemberRepository;
import gift.member.MemberRequestDto;
import gift.option.Option;
import gift.option.OptionRepository;
import gift.option.OptionService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final KakaoClient kakaoClient;
    private final OptionService optionService;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, MemberRepository memberRepository, KakaoClient kakaoClient, OptionService optionService) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.kakaoClient = kakaoClient;
        this.optionService = optionService;
    }

    public OrderResponseDto createOrder(MemberRequestDto memberRequestDto, OrderRequestDto orderRequestDto) {
        Option option = optionRepository.findById(orderRequestDto.optionId())
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));

        optionService.substractQuantity(option.getId(), orderRequestDto.quantity());

        Order order = new Order(option, orderRequestDto.quantity(), orderRequestDto.message(), new Member(memberRequestDto.email(), memberRequestDto.password()));

        orderRepository.save(order);

        kakaoClient.sendMessage(memberRequestDto.email(), orderRequestDto.message());

        return new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

    public List<OrderResponseDto> getOrders(MemberRequestDto memberRequestDto)
        throws NotFoundMember {
        Member member = memberRepository.findByEmail(memberRequestDto.email())
            .orElseThrow(() -> new NotFoundMember("회원정보가 올바르지 않습니다."));

        List<Order>  orders = orderRepository.findAllByMemberId(member.getId());

        return orders.stream()
            .map(order -> new OrderResponseDto(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage()))
            .collect(Collectors.toList());

    }

    public void deleteOrder(MemberRequestDto memberRequestDto, Long orderId) throws NotFoundMember {
        Member member = memberRepository.findByEmail(memberRequestDto.email())
            .orElseThrow(() -> new NotFoundMember("회원정보가 올바르지 않습니다."));

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundOrder("유효하지 않은 주문입니다."));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new NotFoundMember("유효하지 않은 주문입니다.");
        }

        orderRepository.delete(order);
    }

}
