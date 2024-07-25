package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.auth.OAuthLoginMember;
import gift.product.dto.order.OrderDto;
import gift.product.model.Category;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.model.Product;
import gift.product.repository.AuthRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.repository.WishRepository;
import gift.product.service.OrderService;
import java.io.IOException;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    MockWebServer mockWebServer;
    ObjectMapper objectMapper;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OptionRepository optionRepository;

    @Mock
    WishRepository wishRepository;

    @Mock
    AuthRepository authRepository;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void ObjectMapper_셋팅() {
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void 가짜_API_서버_구동() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void 가짜_API_서버_종료() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void 주문_전체_조회() {
        //given
        OAuthLoginMember oAuthLoginMember = new OAuthLoginMember(1L, "test_access_token");

        //when
        orderService.getOrderAll(oAuthLoginMember);

        //then
        then(orderRepository).should().findAllByMemberId(oAuthLoginMember.id());
    }

    @Test
    void 주문_조회() {
        //given
        OAuthLoginMember oAuthLoginMember = new OAuthLoginMember(1L, "test_access_token");
        Order order = new Order(1L, 1L, 1, "test_message");
        given(orderRepository.findByIdAndMemberId(1L, 1L)).willReturn(Optional.of(order));

        //when
        orderService.getOrder(1L, oAuthLoginMember);

        //then
        then(orderRepository).should().findByIdAndMemberId(1L, oAuthLoginMember.id());
    }

    @Test
    void 주문() {
        //given
        OrderDto orderDto = new OrderDto(1L, 3, "test_message");
        OAuthLoginMember oAuthLoginMember = new OAuthLoginMember(1L, "test_access_token");
        Order order = new Order(1L, orderDto.optionId(), 1L, 2, orderDto.message());
        Category category = new Category(1L, "테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);

        String resultCode = "{\"result_code\":0}";
        mockWebServer.enqueue(new MockResponse().setBody(resultCode));

        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        given(authRepository.existsById(any())).willReturn(true);
        given(wishRepository.existsByProductIdAndMemberId(any(), any())).willReturn(true);
        given(orderRepository.save(any())).willReturn(order);

        //when
        String mockUrl = mockWebServer.url("/v2/api/talk/memo/default/send").toString();
        Order resultOrder = orderService.doOrder(orderDto, oAuthLoginMember, mockUrl);

        //then
        assertThat(resultOrder.getId()).isNotNull();
    }

    @Test
    void 주문_삭제() {
        //given
        OAuthLoginMember oAuthLoginMember = new OAuthLoginMember(1L, "test_access_token");
        Order order = new Order(1L, 1L, 1, "test_message");
        given(orderRepository.existsById(1L)).willReturn(true);

        //when
        orderService.deleteOrder(1L, oAuthLoginMember);

        //then
        then(orderRepository).should().deleteByIdAndMemberId(1L, oAuthLoginMember.id());
    }

    @Test
    void 옵션_수량보다_더_많이_차감() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 1, product);
        OrderDto orderDto = new OrderDto(1L, 999, "test_message");
        OAuthLoginMember oAuthLoginMember = new OAuthLoginMember(1L, "test_access_token");
        given(optionRepository.findById(1L)).willReturn(Optional.of(option));
        given(authRepository.existsById(any())).willReturn(true);

        //when, then
        assertThatThrownBy(
            () -> orderService.doOrder(orderDto, oAuthLoginMember, "test_url")).isInstanceOf(
            IllegalArgumentException.class);
    }
}
