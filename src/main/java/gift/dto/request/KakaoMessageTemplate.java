package gift.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoMessageTemplate {
    @JsonProperty("object_type")
    private String objectType = "text";

    @JsonProperty("text")
    private String text;

    @JsonProperty("link")
    private Link link = new Link("https://developers.kakao.com", "https://developers.kakao.com");

    @JsonProperty("button_title")
    private String buttonTitle = "자세히 보기";

    public KakaoMessageTemplate(String text) {
        this.text = text;
    }

    private record Link(
            @JsonProperty("web_url")
            String webUrl,

            @JsonProperty("mobile_web_url")
            String mobileWebUrl
    ) {
    }
}

