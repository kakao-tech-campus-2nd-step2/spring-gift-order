package gift.domain.message;

public record KakaoMessageTemplate(
    String object_type,
    String text,
    KakakoMessageLink link,
    String button_title
) {

}
