package gift.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoInfoDto(
        @JsonProperty("id")Long id,
        @JsonProperty("kakao_account") KakoAccount kakaoAccount
)
{
    @JsonIgnoreProperties(ignoreUnknown = true)
     public record KakoAccount(
            @JsonProperty("name")String name,
            @JsonProperty("email")String email
            )
    {}
}
