package gift.domain.product;

import gift.domain.BaseTimeEntity;
import gift.domain.category.Category;
import gift.domain.option.Option;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    private int price;
    private String imageUrl;

    // JPA 사용을 위한 기본 생성자
    protected Product() {
    }

    public Product(String name, Category category, int price, String imageUrl) {
        validateNotContainKaKao(name);
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, Category category, int price, String imageUrl) {
        validateNotContainKaKao(name);
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean hasOneOption() {
        return options.size() == 1;
    }

    public void update(String name, Category category, int price, String imageUrl) {
        validateNotContainKaKao(name);
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
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

    private void validateNotContainKaKao(String name) {
        if (name.toLowerCase().contains("카카오")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                "'카카오' 문구를 포함할 수 없습니다. 담당 MD와 협의하세요.");
        }
    }
}
