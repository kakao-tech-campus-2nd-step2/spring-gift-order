package gift.service.dto;

import gift.model.Orders;

public class KakaoRequest {
    private static final String DEFAULT_OBJECT_TYPE = "feed";
    private static final String DEFAULT_PROFILE_TEXT = "선물하기";
    private static final String DEFAULT_SUM_TEXT = "합계";
    private static final String DEFAULT_TITLE = "주문해주셔서 감사합니다.";
    private static final String DEFAULT_ITEM_TEXT = "가격";
    private static final String DEFAULT_ITEM_COUNT_TEXT = "개수";

    public record Feed (
            String object_type,
            Content content,
            ItemContent item_content
    ){
        public static Feed from(Orders orders) {
            return new Feed(
                    DEFAULT_OBJECT_TYPE,
                    new Content(
                            DEFAULT_TITLE,
                            orders.getDescription(),
                            null
                    ),
                    new ItemContent(
                            DEFAULT_PROFILE_TEXT,
                            orders.getProductName(),
                            orders.getOptionName(),
                            new Item[]{
                                    new Item(
                                            DEFAULT_ITEM_TEXT,
                                            orders.getPrice() + "원"
                                    ),
                                    new Item(
                                            DEFAULT_ITEM_COUNT_TEXT,
                                            orders.getQuantity() + "개"
                                    ),
                            },
                            DEFAULT_SUM_TEXT,
                            orders.getTotalPrice() + "원"
                    )
            );
        }
    }

    record Content(
            String title,
            String description,
            Link[] link
    ){}

    record ItemContent(
            String profile_text,
            String title_image_text,
            String title_image_category,
            Item[] items,
            String sum,
            String sum_op
    ){}

    record Item(
            String item,
            String item_op
    ){}

    record Link() {}
}
