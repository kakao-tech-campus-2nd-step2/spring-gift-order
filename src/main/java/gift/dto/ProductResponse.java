package gift.dto;

import gift.model.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory().getId();
    }
}
