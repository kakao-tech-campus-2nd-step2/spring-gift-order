package gift.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class MenuName {

    @Column(nullable = false)
    private String value;

    public MenuName() {
    }

    public MenuName(String value) {
        this.value = value;

        if (this.value.length() > 15) {
            throw new IllegalArgumentException("상품명은 15자 이하여야 합니다");
        }
        if (this.value.contains("카카오")) {
            throw new IllegalArgumentException("'카카오'가 포함된 상품명은 담당 MD와 협의해주세요");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuName menuName = (MenuName) o;
        return Objects.equals(value, menuName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
