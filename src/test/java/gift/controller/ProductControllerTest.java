//package gift.controller;
//
//import gift.dto.OptionDto;
//import gift.dto.ProductDto;
//import gift.entity.Category;
//import gift.entity.Product;
//import gift.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class ProductControllerTest {
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private ProductController productController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
//
//        mockMvc = MockMvcBuilders.standaloneSetup(productController)
//                .setCustomArgumentResolvers(pageableArgumentResolver)
//                .build();
//    }
//
//    /** getProducts() 테스트 실패 -> 수정필요
//     * @Test
//     * public void testGetProducts() throws Exception {}
//     * **/
//
//    @Test
//    public void testFindAll() throws Exception {
//        List<ProductDto> productDtos = Collections.emptyList();
//        when(productService.findAll()).thenReturn(productDtos);
//
//        mockMvc.perform(get("/product/all")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isEmpty());
//
//        verify(productService, times(1)).findAll();
//    }
//
//    @Test
//    public void testGetProductById() throws Exception {
//        ProductDto productDto = new ProductDto();
//        when(productService.findById(anyLong())).thenReturn(Optional.of(productDto));
//
//        mockMvc.perform(get("/product/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").exists());
//
//        verify(productService, times(1)).findById(anyLong());
//    }
//
//    @Test
//    public void testAddProduct() throws Exception {
//        ProductDto productDto = new ProductDto();
//        when(productService.save(any(ProductDto.class))).thenReturn(1L);
//
//        mockMvc.perform(post("/product")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"test\"}"))
//                .andExpect(status().isOk());
//
//        verify(productService, times(1)).save(any(ProductDto.class));
//    }
//
//    @Test
//    public void testUpdateProduct() throws Exception {
//        ProductDto productDto = new ProductDto();
//
//        mockMvc.perform(put("/product/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"test\"}"))
//                .andExpect(status().isOk());
//
//        verify(productService, times(1)).update(anyLong(), any(ProductDto.class));
//    }
//
//    @Test
//    public void testDeleteProduct() throws Exception {
//        mockMvc.perform(delete("/product/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(productService, times(1)).delete(anyLong());
//    }
//
//    @Test
//    public void testGetProductOptions() throws Exception {
//        List<OptionDto> options = Collections.emptyList();
//        when(productService.getProductOptions(anyLong())).thenReturn(options);
//
//        mockMvc.perform(get("/product/{productId}/option", 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isEmpty());
//
//        verify(productService, times(1)).getProductOptions(anyLong());
//    }
//
//    @Test
//    public void testSubtractOptionQuantity() throws Exception {
//        mockMvc.perform(post("/product/{productId}/option/{optionId}/subtract", 1L, 1L)
//                        .param("quantity", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(productService, times(1)).subtractOptionQuantity(anyLong(), anyLong(), anyInt());
//    }
//}