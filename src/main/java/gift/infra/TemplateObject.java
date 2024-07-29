package gift.infra;

import org.hibernate.validator.constraints.Length;

public record TemplateObject(String object_type, @Length(min = 1, max = 200) String text, Link link) {
    /*
    web_url : PC버전 카카오톡에서 사용하는 웹 링크 URL
    mobile_web_url : 모바일 카카오톡에서 사용하는 웹 링크 URL
     */
    public record Link(String web_url, String mobile_web_url) {
    }
}
