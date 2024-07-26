package gift.application.member.service.apicaller;

import static io.jsonwebtoken.Header.CONTENT_TYPE;

import gift.application.member.dto.MemberKakaoModel;
import gift.global.validate.TimeOutException;
import java.net.URI;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class MemberKakaoApiCaller {

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    private final RestTemplate restTemplate;

    public MemberKakaoApiCaller(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(TIMEOUT)
            .setReadTimeout(TIMEOUT)
            .build();
    }

    /**
     * 토큰을 사용해서 사용자 정보를 가져옴
     */
    public MemberKakaoModel.MemberInfo getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(KAKAO_USER_INFO_URL));

        try {
            MemberKakaoModel.MemberInfo memberInfo = restTemplate.exchange(request,
                MemberKakaoModel.MemberInfo.class).getBody();
            return memberInfo;
        } catch (ResourceAccessException e) {
            throw new TimeOutException("네트워크 연결이 불안정 합니다.", e);
        }
    }
}
