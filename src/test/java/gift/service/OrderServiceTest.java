package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishService wishService;

    @Mock
    private KakaoService kakaoService;

    @Mock
    private KakaoTokenService kakaoTokenService;

    @InjectMocks
    private OrderService orderService;

    private Member member;
    private Option option;
    private OrderRequestDTO orderRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new Member("test@example.com", "password");
        option = new Option("Test Option", 10, null);

        // 기본 생성자를 사용하고 setter 메서드로 필드를 설정합니다.
        orderRequest = new OrderRequestDTO();
        orderRequest.setOptionId(1L);
        orderRequest.setQuantity(2);
        orderRequest.setMessage("Please handle this order with care.");

        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }


    @Test
    void createOrder_optionNotFound() {
        when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(member, orderRequest);
        });

        assertEquals("Option not found", exception.getMessage());
    }

    @Test
    void createOrder_insufficientQuantity() {
        option = new Option("Test Option", 1, null);
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(member, orderRequest);
        });

        assertEquals("Insufficient option quantity", exception.getMessage());
    }
}
