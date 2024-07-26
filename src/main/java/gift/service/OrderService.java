package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Member;
import gift.domain.Order;
import gift.dto.KakaoMessageDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.kakaoService = kakaoService;
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order(orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message());
        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.convertToDto(savedOrder);
    }

    public void sendOrderMessage(OrderRequestDto orderRequestDto, Member member) {
        try {
            kakaoService.sendKakaoMessageToMe(
                member.getAccessToken(),
                new KakaoMessageDto(
                    "text",
                    orderRequestDto.message(),
                    "https://gift.kakao.com",
                    "https://gift.kakao.com"
                )
            );
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
