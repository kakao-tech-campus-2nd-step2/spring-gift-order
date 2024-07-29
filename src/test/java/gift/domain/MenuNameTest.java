package gift.domain;

import gift.domain.menu.MenuName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuNameTest {

    @DisplayName("상품 명은 15자 이하여야한다. 초과하면 예외가 발생한다.")
    @Test
    void nameLengthTest() {
        assertThatThrownBy(() -> new MenuName("1234567890" + "123456"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품명은 15자 이하여야 합니다");
    }

    @DisplayName("상품 명에 '카카오'가 포함되면 예외가 발생한다.")
    @Test
    void nameContainsKakaoTest() {
        assertThatThrownBy(() -> new MenuName("카카오"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("'카카오'가 포함된 상품명은 담당 MD와 협의해주세요");
    }

    @DisplayName("내부 값이 같으면, 서로 같은 객체로 인식한다.")
    @Test
    void equalsTest() {
        MenuName menuName1 = new MenuName("테스트");
        MenuName menuName2 = new MenuName("테스트");
        assertThat(menuName1).isEqualTo(menuName2);
    }
}
