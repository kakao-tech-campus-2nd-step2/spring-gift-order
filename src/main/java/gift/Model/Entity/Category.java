package gift.Model.Entity;


import jakarta.persistence.*;

@Entity
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private String imageUrl;
    private String description;


    protected Category (){}

    public Category(String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        validateDescription(description);

        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void validateName(String name) {
        if (name == null|| name.isBlank())
            throw new IllegalArgumentException("카테고리 이름은 필수입니다");
    }

    public void validateColor(String color) {
        if (color == null || color.isBlank())
            throw new IllegalArgumentException("카테고리 색상 값은 필수입니다");

        if (color.length() != 7)
            throw new IllegalArgumentException("카테고리 색상 값의 길이는 7입니다");
    }

    public void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank())
            throw new IllegalArgumentException("imageUrl를 입력해주세요");
    }

    public void validateDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("description의 값은 null이면 안됩니다");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void update(String name, String color, String imageUrl, String description){
        this.name = name;
        this.color = color;
        this. imageUrl = imageUrl;
        this.description = description;
    }
}
