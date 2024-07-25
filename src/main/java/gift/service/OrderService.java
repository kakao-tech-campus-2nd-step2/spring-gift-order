package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.dto.request.MemberRequest;
import gift.dto.request.OrderRequest;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoMemberService kakaoMemberService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService, KakaoMemberService kakaoMemberService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoMemberService = kakaoMemberService;
    }

    @Transactional
    public void orderOption(OrderRequest orderRequest, MemberRequest memberRequest, String accessToken){
        optionService.subtractQuantityById(orderRequest.optionId(), orderRequest.quantity());

        save(memberRequest, orderRequest);

        if(wishService.existsByOptionId(orderRequest.optionId())){
            wishService.deleteByOptionId(orderRequest.optionId());
        }

        String message = "옵션 id " + orderRequest.optionId() + " 상품이 주문되었습니다.";
        kakaoMemberService.sendKakaoMessage(accessToken,message);

    }

    @Transactional
    public void save(MemberRequest memberRequest, OrderRequest orderRequest){
        LocalDateTime orderDateTime = LocalDateTime.now();
        Member member = memberRequest.toEntity();
        Option option = optionService.findById(orderRequest.optionId()).toEntity();

        Order order =  orderRequest.toEntity(orderDateTime, member, option);
        orderRepository.save(order);
    }
}
