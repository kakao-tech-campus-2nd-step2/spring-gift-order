package gift.controller.api;

import gift.dto.request.OrderRequest;
import gift.dto.response.OrderResponse;
import gift.service.KakaoApiService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class OrderControllerTest {
    @InjectMocks
    OrderController orderController;
    @Mock
    private OptionService optionService;
    @Mock
    private WishService wishService;
    @Mock
    private KakaoApiService kakaoApiService;
    @Mock
    private OrderService orderService;

    @Test
    void order() {
        //Given
        Long memberId = 1L;
        Long productId = 1L;
        OrderRequest orderRequest = new OrderRequest(1L, 10, "조심히 배송");
        OrderResponse orderResponse = new OrderResponse(1L, 1L, 10, null, "조심히 배송");
        when(orderService.saveOrder(orderRequest)).thenReturn(orderResponse);
        when(optionService.getProductIdByOptionId(orderRequest)).thenReturn(productId);

        //When
        ResponseEntity<OrderResponse> response = orderController.order(memberId, orderRequest);

        //Then
        assertThat(response).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("조심히 배송");
    }
}