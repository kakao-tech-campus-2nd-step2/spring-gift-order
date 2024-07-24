package gift.test.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import gift.controller.KakaoAuthController;
import gift.service.KakaoAuthService;

@WebMvcTest(KakaoAuthController.class)
public class KakaoAuthTest {

	private final MockMvc mockMvc;
	
	@Autowired
	public KakaoAuthTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	@MockBean
    private KakaoAuthService kakaoAuthService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
    public void testKakaoRedirect() throws Exception {
        String authorizationCode = "dummyAuthorizationCode";
        Map<String, String> accessTokenResponse = new HashMap<>();
        accessTokenResponse.put("access_token", "dummy_access_token");
        
        when(kakaoAuthService.getAccessToken(anyString())).thenReturn(accessTokenResponse);

        mockMvc.perform(get("/kakao/redirect")
        		.param("code", authorizationCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("dummy_access_token"));
    }
}
