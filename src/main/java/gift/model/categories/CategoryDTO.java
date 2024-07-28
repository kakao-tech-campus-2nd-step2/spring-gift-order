package gift.model.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "카테고리 데이터 전송 객체")
public class CategoryDTO {

    @Schema(description = "카테고리 ID(카테고리 추가 시 입력 불필요)", example = "1")
    private final Long id;

    @Schema(description = "카테고리 이름", example = "전자기기")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣() +\\-&\\[\\]/_]*", message = "(),[],+,-,&,/,_ 를 제외한 특수문자는 사용이 불가합니다.")
    private final String name;

    @Schema(description = "카테고리 이미지 URL", example = "https://example.com/images/electronics.jpg")
    private final String imgUrl;

    public CategoryDTO(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Category toEntity() {
        return new Category(id, name, imgUrl);
    }
}
