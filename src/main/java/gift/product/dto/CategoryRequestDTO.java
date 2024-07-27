package gift.product.dto;

import gift.product.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDTO {

    private Long id;
    @NotBlank(message = "카테고리 이름은 필수 입력사항입니다.")
    private String name;

    public CategoryRequestDTO() {}
    public CategoryRequestDTO(String name) {}
    public CategoryRequestDTO(Long id, String name) {}

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Category convertToDomain() {
        return new Category(this.name);
    }
}
