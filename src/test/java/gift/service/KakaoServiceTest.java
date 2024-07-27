package gift.service;

import gift.config.WebClientUtil;
import gift.dto.KakaoTokenResponseDto;
import gift.model.member.KakaoProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KakaoServiceTest {

    @Mock
    private WebClientUtil webClientUtil;

    @Mock
    private KakaoProperties kakaoProperties;

    @InjectMocks
    private KakaoService kakaoService;

    @Test
    public void testGetAccessTokenFromKakao() throws Exception {
    }
}