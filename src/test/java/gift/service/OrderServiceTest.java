package gift.service;

import gift.config.RestTemplateConfig;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.Wish;
import gift.domain.member.Member;
import gift.dto.OrderDto;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.service.kakao.Oauth2TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = OrderService.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OptionRepository optionRepository;

    @MockBean
    private WishRepository wishRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Oauth2TokenService oauth2TokenService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("주문을 처리하고 카카오 메시지를 성공적으로 전송한다.")
    @Test
    void processOrderSuccessfully() throws Exception {
        //given
        Member member = new Member();
        member.setAccessToken("test-access-token");

        Long originalQuantity = 10L;
        Long orderQuantity = 5L;
        String message = "Test Message";

        OrderDto orderDto = new OrderDto(1L, 2L, orderQuantity, member, message);

        Option option = new Option();
        option.setProduct(new Product());
        option.setId(1L);
        option.setQuantity(originalQuantity);

        String expectedKakaoResponse = "{}";

        given(oauth2TokenService.isAccessTokenExpired(anyString())).willReturn(false);
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(wishRepository.findByMemberAndProduct(any(Member.class), any(Product.class))).willReturn(Optional.of(new Wish()));
        willDoNothing().given(wishRepository).delete(any(Wish.class));
        given(orderRepository.save(any(Order.class))).willReturn(new Order());

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
                .andExpect(method(POST))
                .andExpect(header("Authorization", "Bearer test-access-token"))
                .andRespond(withSuccess(expectedKakaoResponse, APPLICATION_JSON));

        //when
        OrderDto result = orderService.processOrder(orderDto);

        //then
        assertThat(result.getQuantity()).isEqualTo(originalQuantity - orderQuantity);
        assertThat(result.getMessage()).isEqualTo(message);

        then(oauth2TokenService).should().isAccessTokenExpired(anyString());
        then(optionRepository).should().findById(anyLong());
        then(wishRepository).should().findByMemberAndProduct(any(Member.class), any(Product.class));
        then(wishRepository).should().delete(any(Wish.class));
        then(orderRepository).should().save(any(Order.class));
        mockServer.verify();
    }

    @DisplayName("토큰이 만료되어 갱신 후 카카오 메시지를 전송한다.")
    @Test
    void processOrderWithTokenRefresh() throws Exception {
        //given
        Member member = new Member();
        member.setAccessToken("test-access-token");

        Long originalQuantity = 10L;
        Long orderQuantity = 5L;
        String message = "Test Message";

        OrderDto orderDto = new OrderDto(1L, 2L, orderQuantity, member, message);

        Option option = new Option();
        option.setProduct(new Product());
        option.setId(1L);
        option.setQuantity(originalQuantity);

        String expectedKakaoResponse = "{}";

        given(oauth2TokenService.isAccessTokenExpired(anyString())).willReturn(true);
        willDoNothing().given(oauth2TokenService).refreshAccessToken(any(Member.class));
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(wishRepository.findByMemberAndProduct(any(Member.class), any(Product.class))).willReturn(Optional.of(new Wish()));
        willDoNothing().given(wishRepository).delete(any(Wish.class));
        given(orderRepository.save(any(Order.class))).willReturn(new Order());

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
                .andExpect(method(POST))
                .andExpect(header("Authorization", "Bearer test-access-token"))
                .andRespond(withSuccess(expectedKakaoResponse, APPLICATION_JSON));

        //when
        OrderDto result = orderService.processOrder(orderDto);

        //then
        assertThat(result.getQuantity()).isEqualTo(originalQuantity - orderQuantity);
        assertThat(result.getMessage()).isEqualTo(message);

        then(oauth2TokenService).should().isAccessTokenExpired(anyString());
        then(oauth2TokenService).should().refreshAccessToken(any(Member.class));
        then(optionRepository).should().findById(anyLong());
        then(wishRepository).should().findByMemberAndProduct(any(Member.class), any(Product.class));
        then(wishRepository).should().delete(any(Wish.class));
        then(orderRepository).should().save(any(Order.class));
        mockServer.verify();
    }

}
