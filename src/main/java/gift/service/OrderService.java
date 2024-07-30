package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String email) {
        logger.info("Creating order for email: {}", email);

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(() -> new RuntimeException("유효하지 않은 회원입니다."));

        logger.info("Member found: {}", member.getEmail());

        Option option = optionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        logger.info("Option found: {}", option.getId());

        option.subtractQuantity(request.getQuantity());
        optionRepository.save(option);

        Order order = new Order(option, request.getQuantity(), LocalDateTime.now(), request.getMessage());
        orderRepository.save(order);

        logger.info("Order saved with ID: {}", order.getId());

        return new OrderResponse(
                order.getId(),
                option.getId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }

    public OrderResponse processOrderAndSendMessage(OrderRequest orderRequest, String email) {
        // 트랜잭션 내에서 주문을 생성
        return createOrder(orderRequest, email);

        /** 카카오 메시지 전송 부분은 필요 시 활성화
        String bearerToken = sendMessageRequest.getBearerToken();
        OrderRequest orderRequest = sendMessageRequest.getOrderRequest();

        logger.info("Received sendMessageToMe request with Authorization: {}", bearerToken);
        logger.info("OrderRequest: {}", orderRequest);

        // 트랜잭션 내에서 주문을 생성
        OrderResponse orderResponse = createOrder(orderRequest);

        // 트랜잭션 외부에서 카카오 메시지 전송
        String accessToken = sendMessageRequest.getAccessToken();
        kakaoMessageService.sendMessage(orderResponse, accessToken); **/
    }
}