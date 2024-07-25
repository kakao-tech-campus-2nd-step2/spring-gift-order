package gift.service;

import gift.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserService userService;


    @Test
    void orderSaveTest() {
        // given
        String testEmail = "test@gmail.com";
        userService.signup(new UserDTO(testEmail, "test"));

        Product product1 = productService.save(new ProductDTO("test1", 123, "test.com", 1L));
        Product product2 = productService.save(new ProductDTO("test2", 123, "test.com", 1L));

        wishlistService.addWishlistProduct(testEmail, new WishlistDTO(product1.getId()));
        wishlistService.addWishlistProduct(testEmail, new WishlistDTO(product2.getId()));

        List<Product> products = wishlistService.getWishlistProducts(testEmail, PageRequest.of(0, 10)).getContent();
        Option option = optionService.save(new OptionDTO("abc", 100));
        productService.addProductOption(product1.getId(), option.getId());

        // when
        orderService.save(testEmail, new OrderDTO(product1.getId(), option.getId(), 30, "test msg"));

        // then
        // 옵션 quantity 차감 확인
        Option expectOption = optionService.findById(option.getId());
        assertThat(expectOption.getQuantity()).isEqualTo(70);
        // wishlist에서 삭제 확인
        List<Product> expectProducts = wishlistService.getWishlistProducts(testEmail, PageRequest.of(0, 10)).getContent();
        assertThat(expectProducts.size()).isEqualTo(1);
    }


}
