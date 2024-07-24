package gift.main.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.jsonwebtoken.security.Password;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "spring.kakao") //내부적으로 세터를 이용해서 등록하게 한다.
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoProperties (
     String grantType,
     String codeRequestUri,
     String tokenRequestUri,
     String userRequestUri,
     String redirectUri,
     String clientId,
     String password){
}

