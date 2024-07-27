package gift.service;

import gift.config.WebClientUtil;
import gift.dto.OrderRequestDto;
import gift.model.member.KakaoProperties;
import gift.repository.MemberRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
public class OrderServiceTest {

    @InjectMocks
    private KakaoService kakaoService;

    @Mock
    private WebClientUtil webClientUtil;

    @Mock
    private KakaoProperties kakaoProperties;

    @Mock
    private MemberRepository memberRepository;
}