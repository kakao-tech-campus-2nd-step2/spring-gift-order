package gift.service;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.entity.Order;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private KakaoService kakaoService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        // Given
        OrderRequest orderRequest = new OrderRequest.Builder()
                .optionId(1L)
                .quantity(2)
                .message("Please handle this order with care.")
                .build();

        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);
        when(order.getOptionId()).thenReturn(orderRequest.getOptionId());
        when(order.getQuantity()).thenReturn(orderRequest.getQuantity());
        when(order.getMessage()).thenReturn(orderRequest.getMessage());
        when(order.getOrderDateTime()).thenReturn(null);  // 추가: getOrderDateTime()에 대한 모킹도 필요

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        // Then
        assertEquals(1L, orderResponse.getOptionId());
        assertEquals(2, orderResponse.getQuantity());
        assertEquals("Please handle this order with care.", orderResponse.getMessage());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(wishRepository, times(1)).deleteByOptionId(1L);
        verify(kakaoService, times(1)).sendKakaoMessage(any(OrderResponse.class));
    }
}