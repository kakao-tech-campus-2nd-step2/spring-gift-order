package gift.entity;

import gift.validation.constraint.NameConstraint;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

    @NameConstraint
    @NotNull
    private String name;
    @NotNull
    private Integer price;
    @NotNull
    private String imageurl;
    @NotNull
    private Long category_id;

    public ProductDTO(String name, Integer price, String imageurl, Long category_id) {
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        this.category_id = category_id;
    }

    public ProductDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Long getCategoryid() {
        return category_id;
    }

    public void setCategoryid(Long category_id) {
        this.category_id = category_id;
    }
}
