package gift.service;

import gift.model.member.KakaoProperties;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

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