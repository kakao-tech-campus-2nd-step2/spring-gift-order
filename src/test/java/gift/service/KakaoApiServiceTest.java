package gift.service;

import gift.controller.dto.KakaoApiDTO;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.Token;
import gift.domain.UserInfo;
import gift.domain.Wish;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.TokenRepository;
import gift.repository.UserInfoRepository;
import gift.repository.WishRepository;
import gift.utils.ExternalApiService;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoApiServiceTest {

    @Mock private OptionRepository optionRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private UserInfoRepository userInfoRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private ExternalApiService externalApiService;
    @Mock private WishRepository wishRepository;

    @InjectMocks
    private KakaoApiService kakaoApiService;

    @Test
    @DisplayName("주문 성공 상황")
    void kakaoOrder_Success() {
        // Given
        Long optionId = 1L;
        int quantity = 2;
        String message = "Test message";
        String accessToken = "test_access_token";

        KakaoApiDTO.KakaoOrderRequest request = new KakaoApiDTO.KakaoOrderRequest(optionId, quantity, message);

        Product product = new Product();
        product.setId(1L);

        Option option = new Option();
        option.setId(optionId);
        option.setQuantity(10);
        option.setProduct(product);

        Token token = new Token();
        token.setEmail("test@example.com");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setEmail("test@example.com");

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(tokenRepository.findByToken(accessToken)).thenReturn(token);
        when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userInfo));
        when(wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId())).thenReturn(Optional.empty());

        // When
        KakaoApiDTO.KakaoOrderResponse response = kakaoApiService.kakaoOrder(request, accessToken);

        // Then
        assertNotNull(response);
        assertEquals(optionId, response.optionId());
        assertEquals(quantity, response.quantity());
        assertEquals(message, response.message());

        verify(orderRepository).save(any(Order.class));
        verify(externalApiService).postSendMe(any(KakaoApiDTO.KakaoOrderResponse.class), eq(accessToken));
        verify(wishRepository).findByUserInfoIdAndProductId(userInfo.getId(), product.getId());
        verify(wishRepository, never()).delete(any());

        assertEquals(8, option.getQuantity());
    }

    @Test
    @DisplayName("주문 성공 및 위시리스트 삭제 상황")
    void kakaoOrder_SuccessWithWishDelete() {
        // Given
        Long optionId = 1L;
        int quantity = 2;
        String message = "Test message";
        String accessToken = "test_access_token";

        KakaoApiDTO.KakaoOrderRequest request = new KakaoApiDTO.KakaoOrderRequest(optionId, quantity, message);

        Product product = new Product();
        product.setId(1L);

        Option option = new Option();
        option.setId(optionId);
        option.setQuantity(10);
        option.setProduct(product);

        Token token = new Token();
        token.setEmail("test@example.com");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setEmail("test@example.com");

        Wish wish = new Wish();

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(tokenRepository.findByToken(accessToken)).thenReturn(token);
        when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userInfo));
        when(wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId())).thenReturn(Optional.of(wish));

        // When
        KakaoApiDTO.KakaoOrderResponse response = kakaoApiService.kakaoOrder(request, accessToken);

        // Then
        assertNotNull(response);
        verify(wishRepository).delete(wish);
    }

    @Test
    @DisplayName("옵션을 찾을 수 없는 상황")
    void kakaoOrder_OptionNotFound() {
        // Given
        Long optionId = 1L;
        KakaoApiDTO.KakaoOrderRequest request = new KakaoApiDTO.KakaoOrderRequest(optionId, 2, "Test");

        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OptionNotFoundException.class, () ->
            kakaoApiService.kakaoOrder(request, "test_token")
        );
    }

    @Test
    @DisplayName("유저를 찾을 수 없는 상황")
    void kakaoOrder_UserNotFound() {
        // Given
        Long optionId = 1L;
        KakaoApiDTO.KakaoOrderRequest request = new KakaoApiDTO.KakaoOrderRequest(optionId, 2, "Test");
        String accessToken = "test_token";

        Option option = new Option();
        option.setId(optionId);

        Token token = new Token();
        token.setEmail("test@example.com");

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(tokenRepository.findByToken(accessToken)).thenReturn(token);
        when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () ->
            kakaoApiService.kakaoOrder(request, accessToken)
        );
    }

    @Test
    @DisplayName("재고가 부족한 상황")
    void kakaoOrder_InsufficientQuantity() {
        // Given
        Long optionId = 1L;
        int requestQuantity = 5;
        KakaoApiDTO.KakaoOrderRequest request = new KakaoApiDTO.KakaoOrderRequest(optionId, requestQuantity, "Test");
        String accessToken = "test_token";

        Product product = new Product();
        product.setId(1L);

        Option option = new Option();
        option.setId(optionId);
        option.setQuantity(3); // 요청 수량보다 적은 재고
        option.setProduct(product);

        Token token = new Token();
        token.setEmail("test@example.com");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setEmail("test@example.com");

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(tokenRepository.findByToken(accessToken)).thenReturn(token);
        when(userInfoRepository.findByEmail("test@example.com")).thenReturn(Optional.of(userInfo));

        // When & Then
        assertThrows(IllegalStateException.class, () ->
            kakaoApiService.kakaoOrder(request, accessToken)
        );
    }
}
