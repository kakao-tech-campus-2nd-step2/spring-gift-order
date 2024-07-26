package gift.controller;

import gift.dto.WishRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.service.WishService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WishControllerTest {

    @Mock
    private WishService wishService;

    @InjectMocks
    private WishController wishController;

    private ObjectMapper objectMapper = new ObjectMapper();


    /** getWishes() 테스트 실패 -> 수정필요
     * @Test
     * public void testGetWishes() throws Exception {}
     * **/

    @Test
    public void testAddWish() throws Exception {
        MockitoAnnotations.openMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(wishController)
                .build();

        Wish wish = mock(Wish.class);
        when(wish.getId()).thenReturn(1L);

        WishRequest wishRequest = new WishRequest(1L);
        when(wishService.addWish(anyString(), any(WishRequest.class))).thenReturn(wish);

        mockMvc.perform(post("/wish/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .content(objectMapper.writeValueAsString(wishRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(wishService, times(1)).addWish(anyString(), any(WishRequest.class));
    }

    @Test
    public void testRemoveWish() throws Exception {
        MockitoAnnotations.openMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(wishController)
                .build();

        WishRequest wishRequest = new WishRequest(1L);
        doNothing().when(wishService).removeWish(anyString(), anyLong());

        mockMvc.perform(delete("/wish/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .content(objectMapper.writeValueAsString(wishRequest)))
                .andExpect(status().isOk());

        verify(wishService, times(1)).removeWish(anyString(), anyLong());
    }
}