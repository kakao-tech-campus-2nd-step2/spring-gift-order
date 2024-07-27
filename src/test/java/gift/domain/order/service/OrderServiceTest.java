package gift.domain.order.service;

import gift.auth.AuthProvider;
import gift.domain.order.repository.OrderJpaRepository;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderJpaRepository orderJpaRepository;

    @MockBean
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private WishlistJpaRepository wishlistJpaRepository;

    private static final User user = new User(1L, "testUser", "test@test.com", "test123", Role.USER, AuthProvider.LOCAL);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
    private static final Product product = new Product(1L, category, "아이스 카페 아메리카노 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg");
    private static final Option option = new Option(1L, product, "사과맛", 90);


//    @Test
//    void create() {
//        // given
//        product.addOption(option);
//        OrderRequest orderRequest = new OrderRequest(product.getId(), option.getId(), 10, "테스트 와하하");
//        Order order = new Order(1L, user, product, option, 10, "테스트 와하하");
//        OrderResponse expected = OrderResponse.from(order);
//
//        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(product));
//        doNothing().when(wishlistJpaRepository).deleteByUserAndProduct(any(User.class), any(Product.class));
//        given(orderJpaRepository.save(any(Order.class))).willReturn(order);
//
//        // when
//        OrderResponse actual = orderService.create(orderRequest, user);
//
//        // then
//        assertThat(actual).isEqualTo(expected);
//    }
}