/*
package gift.service;

import gift.dto.OrderDTO;
import gift.model.Option;
import gift.model.Product;
import gift.model.User;
import gift.repository.OptionRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private KakaoService kakaoService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        // Arrange
        User user = new User("test@example.com", "password123");
        Product product = new Product(1L, "Product", 1000, "http://example.com/image.jpg", null);
        Option option = new Option(1L, "Option A", 10, product);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("주문을 처리해주세요.");

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        // Act
        OrderDTO result = orderService.createOrder(orderDTO, user);

        // Assert
        assertEquals(1L, result.getOptionId());
        assertEquals(2, result.getQuantity());
        assertEquals("주문을 처리해주세요.", result.getMessage());
        assertEquals(user.getEmail(), user.getEmail());

        verify(optionRepository, times(1)).save(any(Option.class));
        verify(wishlistRepository, times(1)).deleteByUserEmailAndProductId(eq(user.getEmail()), eq(product.getId()));
        verify(kakaoService, times(1)).sendMessageToMe(eq(user.getEmail()), anyString());
    }

    @Test
    void testCreateOrder_InvalidOptionId() throws Exception {
        // Arrange
        User user = new User("test@example.com", "password123");

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("주문을 처리해주세요.");

        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderDTO, user);
        });

        assertEquals("Order creation failed", exception.getMessage());
        verify(optionRepository, never()).save(any(Option.class));
        verify(wishlistRepository, never()).deleteByUserEmailAndProductId(anyString(), anyLong());
        verify(kakaoService, never()).sendMessageToMe(anyString(), anyString());
    }

    @Test
    void testCreateOrder_InsufficientQuantity() throws Exception {
        // Arrange
        User user = new User("test@example.com", "password123");
        Product product = new Product(1L, "Product", 1000, "http://example.com/image.jpg", null);
        Option option = new Option(1L, "Option A", 1, product);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("주문을 처리해주세요.");

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderDTO, user);
        });

        assertEquals("Order creation failed", exception.getMessage());
        verify(optionRepository, never()).save(any(Option.class));
        verify(wishlistRepository, never()).deleteByUserEmailAndProductId(anyString(), anyLong());
        verify(kakaoService, never()).sendMessageToMe(anyString(), anyString());
    }

    @Test
    void testCreateOrder_MessageSendingFails() throws Exception {
        // Arrange
        User user = new User("test@example.com", "password123");
        Product product = new Product(1L, "Product", 1000, "http://example.com/image.jpg", null);
        Option option = new Option(1L, "Option A", 10, product);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOptionId(1L);
        orderDTO.setQuantity(2);
        orderDTO.setMessage("주문을 처리해주세요.");

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));
        doThrow(new Exception("메시지 전송 실패")).when(kakaoService).sendMessageToMe(anyString(), anyString());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderDTO, user);
        });

        assertEquals("Order creation failed", exception.getMessage());
        verify(optionRepository, times(1)).save(any(Option.class));
        verify(wishlistRepository, times(1)).deleteByUserEmailAndProductId(eq(user.getEmail()), eq(product.getId()));
        verify(kakaoService, times(1)).sendMessageToMe(anyString(), anyString());
    }
}

 */