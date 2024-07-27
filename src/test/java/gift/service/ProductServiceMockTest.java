package gift.service;

import gift.entity.Product;
import gift.entity.ProductOption;
import gift.entity.User;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductOptionRepository productOptionRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품에_옵션이_한개_이하_존재할_때_옵션_삭제_불가() {
        // given
        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setUser(user);

        given(userService.findOne((String) any())).willReturn(user);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        given(productOptionRepository.findByProductId(any())).willReturn(List.of(new ProductOption()));

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.deleteProductOption(product.getId(), user.getId(), "test@naver.com"));
    }

//    @Test
//    @DisplayName("product가 삭제되었을 때 product_wishlist에서 해당 행이 삭제되어야 함")
//    void productDeleteCascadeWishlistTest() {
//        // given
//        String testEmail = "test@gmail.com";
//        Category category = categoryService.save(new CategoryDTO("test", "#test", "test.com", ""));
//        Product product = productService.save(new ProductDTO("test", 123, "test.com", category.getId()));
//        wishlistService.addWishlistProduct(testEmail, new WishlistDTO(product.getId()));
//
//        // when
//        productService.delete(product.getId());
//
//        // then
//        Page<Product> products = wishlistService.getWishlistProducts(testEmail, PageRequest.of(0, 10));
//        assertThat(products).hasSize(0);
//    }
//
//    @Test
//    @DisplayName("특정 product를 wishlist에 담은 유저의 수 확인 테스트")
//    void productWishlistCheckTest() {
//        // given
//        String testEmail1 = "test1@gmail.com";
//        String testEmail2 = "test2@gmail.com";
//        Category category = categoryService.save(new CategoryDTO("test", "#test", "test.com", ""));
//        Product product = productService.save(new ProductDTO("test", 123, "test.com", category.getId()));
//
//        // when
//        wishlistService.addWishlistProduct(testEmail1, new WishlistDTO(product.getId()));
//        wishlistService.addWishlistProduct(testEmail2, new WishlistDTO(product.getId()));
//
//        // then
//        List<Wishlist> wishlists = productService.getProductWishlist(product.getId());
//        assertThat(wishlists).hasSize(2);
//    }
}
