package gift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.api.OrderRequest;
import gift.model.Name;
import gift.model.Option;
import gift.model.OptionName;
import gift.model.OptionQuantity;
import gift.model.Product;
import gift.model.User;
import gift.model.WishList;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import gift.service.KakaoService;
import gift.service.OptionService;
import gift.service.ProductService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KakaoService kakaoService;

    @MockBean
    private OptionService optionService;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        wishListRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
        optionRepository.deleteAll();
    }


    @Test
    @Transactional
    public void createOrder_InsufficientProductQuantity() throws Exception {
        // Set up user
        User user = new User(null, "test@example.com", PasswordEncoder.encode("password"));
        userRepository.save(user);

        // Set up product
        Product product = new Product(null, new Name("Test Product"), 1000, "http://example.com/image.jpg", 1L, new ArrayList<>());
        productRepository.save(product);

        // Set up option
        Option option = new Option(null, new OptionName("Test Option"), new OptionQuantity(10), product);
        optionRepository.save(option);

        // Set up wishlist
        WishList wishList = new WishList(null, user, product);
        wishListRepository.save(wishList);

        // Mock KakaoService to return expected email
        Mockito.when(kakaoService.getUserEmail(Mockito.anyString())).thenReturn("test@example.com");

        // Mock OptionService to fail decreasing option quantity
        Mockito.when(optionService.decreaseOptionQuantity(Mockito.anyLong(), Mockito.anyInt())).thenReturn(false);

        // Prepare order request
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(product.getId());
        orderRequest.setOptionId(option.getId());
        orderRequest.setQuantity(11); // Requesting more than available quantity
        orderRequest.setMessage("Order message");

        // Mock access token
        String accessToken = "some-valid-token";

        // Perform order request
        mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertEquals("Insufficient product quantity.", result.getResponse().getContentAsString()));
    }

    @Test
    @Transactional
    public void createOrder_FailsToSendMessage() throws Exception {
        // Set up user
        User user = new User(null, "test@example.com", PasswordEncoder.encode("password"));
        userRepository.save(user);

        // Set up product
        Product product = new Product(null, new Name("Test Product"), 1000, "http://example.com/image.jpg", 1L, new ArrayList<>());
        productRepository.save(product);

        // Set up option
        Option option = new Option(null, new OptionName("Test Option"), new OptionQuantity(10), product);
        optionRepository.save(option);

        // Set up wishlist
        WishList wishList = new WishList(null, user, product);
        wishListRepository.save(wishList);

        // Mock KakaoService to return expected email
        Mockito.when(kakaoService.getUserEmail(Mockito.anyString())).thenReturn("test@example.com");

        // Mock OptionService to decrease option quantity
        Mockito.when(optionService.decreaseOptionQuantity(Mockito.anyLong(), Mockito.anyInt())).thenReturn(true);

        // Mock ProductService to return product name
        Mockito.when(productService.getProductNameById(Mockito.anyLong())).thenReturn("Test Product");

        // Mock OptionService to return option name
        Mockito.when(optionService.getOptionNameById(Mockito.anyLong())).thenReturn("Test Option");

        // Mock OptionService to return remaining quantity
        Mockito.when(optionService.getRemainingQuantityById(Mockito.anyLong())).thenReturn(9);

        // Mock sendKakaoMessage to fail
        Mockito.when(kakaoService.getUserEmail(Mockito.anyString())).thenReturn("test@example.com");

        // Prepare order request
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(product.getId());
        orderRequest.setOptionId(option.getId());
        orderRequest.setQuantity(1);
        orderRequest.setMessage("Order message");

        // Mock access token
        String accessToken = "some-valid-token";

        // Perform order request
        mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> assertEquals("Order created but failed to send message.", result.getResponse().getContentAsString()));
    }
}