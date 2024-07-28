package gift.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import gift.oauth.KakaoToken;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

//@SpringBootTest
public class SerializeTest {

    @Test
    void objectMapperTest() throws JsonProcessingException {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken kakaoToken = objectMapper.readValue(json(), KakaoToken.class);

        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeMillis());

    }

    @Test
    void gsonTest(){
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Gson gson = new Gson();
        KakaoToken kakaoToken = gson.fromJson(json(), KakaoToken.class);
        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeMillis());

    }

    private String json(){
        return "{"
            + "\"token_type\": \"bearer\", "
            + "\"access_token\": \"Access Token\", "
            + "\"expires_in\": 43199, "
            + "\"refresh_token\": \"Refresh Token\", "
            + "\"refresh_token_expires_in\": 5184000, "
            + "\"scope\": \"account_email profile\""
            + "}";
    }

}