package gift.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.entity.Order;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.product.service.OptionService;
import gift.domain.user.entity.User;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class OrderItemServiceTest {

    @Autowired
    private OrderItemService orderItemService;

    @MockBean
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private WishlistJpaRepository wishlistJpaRepository;

    @MockBean
    private OptionService optionService;


    @Test
    @DisplayName("구매 아이템 생성 테스트")
    void create() {
        // given
        User user = new User(1L, "testUser", "test@test.com", "test123", null, null);
        Category category = new Category(1L, "음식", "#FFFFFF", "https://test.com", "testCategory");
        Product product = new Product(1L, category, "testProduct", 1000, "https://test.com");
        List<Option> options = List.of(
            new Option(1L, product, "첫 번째 옵션", 800),
            new Option(2L, product, "두 번째 옵션", 700),
            new Option(3L, product, "세 번째 옵션", 900)
        );
        options.forEach(product::addOption);
        Order order = new Order(1L, user, "testMessage", 150000);
        List<OrderItemRequest> orderItemRequests = List.of(
            new OrderItemRequest(1L, 1L, 70),
            new OrderItemRequest(1L, 2L, 30),
            new OrderItemRequest(1L, 3L, 50)
        );

        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(optionService.subtractQuantity(eq(1L), eq(70))).willReturn(options.get(0));
        given(optionService.subtractQuantity(eq(2L), eq(30))).willReturn(options.get(1));
        given(optionService.subtractQuantity(eq(3L), eq(50))).willReturn(options.get(2));
        doNothing().when(wishlistJpaRepository).deleteByUserAndProduct(any(User.class), any(Product.class));

        // when
        orderItemService.create(user, order, orderItemRequests);

        // then
        assertThat(order.getOrderItems().size()).isEqualTo(3);
    }
}