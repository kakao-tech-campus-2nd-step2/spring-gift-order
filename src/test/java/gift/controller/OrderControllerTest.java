package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Given
        OrderRequest orderRequest = new OrderRequest(1L, 2, "Please handle this order with care.");

        OrderResponse orderResponse = new OrderResponse.Builder()
                .id(1L)
                .optionId(1L)
                .quantity(2)
                .message("Please handle this order with care.")
                .build();

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        // When & Then
        mockMvc.perform(post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"quantity\":2,\"message\":\"Please handle this order with care.\"}")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"optionId\":1,\"quantity\":2,\"message\":\"Please handle this order with care.\"}"));
    }
}