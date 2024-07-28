package gift.dto;

import java.util.Arrays;
import java.util.List;

public record KakaoTalkRequest(
    String object_type,
    Content content,
    ItemContent item_content
) {

    public record Content(
        String title,
        String image_url,
        Link link
    ) {

    }

    public record Link(
        String web_url
    ) {

    }

    public record ItemContent(
        String profile_text,
        String title_image_text,
        String title_image_category,
        List<ItemObject> items
    ) {

    }

    public record ItemObject(
        String item,
        String item_op
    ) {

    }

    public static KakaoTalkRequest of(String message, String imageUrl, String webUrl,
        String productName, String categoryName, int productPrice) {
        return new KakaoTalkRequest(
            "feed",
            new Content(
                message,
                imageUrl,
                new Link(webUrl)
            ),
            new ItemContent(
                "주문해 주셔서 감사합니다.",
                productName,
                categoryName,
                Arrays.asList(new ItemObject("가격", Integer.toString(productPrice) + "원"))
            )
        );
    }
}
