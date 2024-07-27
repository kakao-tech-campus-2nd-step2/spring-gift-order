package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.order.OrderDto;
import gift.product.model.Category;
import gift.product.model.KakaoToken;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.model.Product;
import gift.product.repository.AuthRepository;
import gift.product.repository.KakaoTokenRepository;
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

    @Mock
    KakaoTokenRepository kakaoTokenRepository;

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
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);

        //when
        orderService.getOrderAll(loginMemberIdDto);

        //then
        then(orderRepository).should().findAllByMemberId(loginMemberIdDto.id());
    }

    @Test
    void 주문_조회() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Order order = new Order(1L, 1L, loginMemberIdDto.id(), 1, "test_message");
        given(orderRepository.findByIdAndMemberId(order.getId(), loginMemberIdDto.id())).willReturn(Optional.of(order));

        //when
        orderService.getOrder(order.getId(), loginMemberIdDto);

        //then
        then(orderRepository).should().findByIdAndMemberId(order.getId(), loginMemberIdDto.id());
    }

    @Test
    void 주문() {
        //given
        OrderDto orderDto = new OrderDto(1L, 3, "test_message");
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Order order = new Order(1L, orderDto.optionId(), loginMemberIdDto.id(), 2, orderDto.message());
        Category category = new Category(1L, "테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 5, product);
        KakaoToken kakaoToken = new KakaoToken(1L,
            loginMemberIdDto.id(),
            "test_oauth_access_token",
            "test_oauth_refresh_token");

        String resultCode = "{\"result_code\":0}";
        mockWebServer.enqueue(new MockResponse().setBody(resultCode));

        given(optionRepository.findById(any())).willReturn(Optional.of(option));
        given(authRepository.existsById(any())).willReturn(true);
        given(wishRepository.existsByProductIdAndMemberId(any(), any())).willReturn(true);
        given(orderRepository.save(any())).willReturn(order);
        given(kakaoTokenRepository.findByMemberId(any())).willReturn(Optional.of(kakaoToken));

        //when
        String mockUrl = mockWebServer.url("/v2/api/talk/memo/default/send").toString();
        Order resultOrder = orderService.doOrder(orderDto, loginMemberIdDto, mockUrl);

        //then
        assertThat(resultOrder.getId()).isNotNull();
    }

    @Test
    void 주문_삭제() {
        //given
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        Order order = new Order(1L, 1L, loginMemberIdDto.id(), 1, "test_message");
        given(orderRepository.existsById(order.getId())).willReturn(true);

        //when
        orderService.deleteOrder(order.getId(), loginMemberIdDto);

        //then
        then(orderRepository).should().deleteByIdAndMemberId(order.getId(), loginMemberIdDto.id());
    }

    @Test
    void 옵션_수량보다_더_많이_차감() {
        //given
        Category category = new Category("테스트카테고리");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        Option option = new Option(1L, "테스트옵션", 1, product);
        OrderDto orderDto = new OrderDto(option.getId(), 999, "test_message");
        LoginMemberIdDto loginMemberIdDto = new LoginMemberIdDto(1L);
        given(optionRepository.findById(option.getId())).willReturn(Optional.of(option));
        given(authRepository.existsById(any())).willReturn(true);

        //when, then
        assertThatThrownBy(
            () -> orderService.doOrder(orderDto, loginMemberIdDto, "test_url")).isInstanceOf(
            IllegalArgumentException.class);
    }
}
