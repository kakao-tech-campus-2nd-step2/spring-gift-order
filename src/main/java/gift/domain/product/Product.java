package gift.domain.product;

import gift.domain.BaseTimeEntity;
import gift.domain.category.Category;
import gift.domain.option.Option;
import gift.global.annotation.NotContainsValue;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotContainsValue(value = "카카오", message = "'{value}' 가 포함된 문구는 담당 MD 와 협의 후 사용 가능합니다.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    private int price;
    private String imageUrl;

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    // JPA 사용을 위한 기본 생성자
    protected Product() {
    }

    public Product(String name, Category category, int price, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        validateProduct();
    }

    public Product(Long id, String name, Category category, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        validateProduct();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean hasOneOption() {
        return options.size() == 1;
    }

    public void update(String name, Category category, int price, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        validateProduct();
    }

    private void validateProduct() {
        Set<ConstraintViolation<Product>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            throw new BusinessException(ErrorCode.BAD_REQUEST, message);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", category=" + category +
               ", options=" + options +
               ", price=" + price +
               ", imageUrl='" + imageUrl + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
               price == product.price &&
               Objects.equals(name, product.name) &&
               Objects.equals(imageUrl, product.imageUrl) &&
               Objects.equals(category.getId(), product.category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price, imageUrl);
    }
}
