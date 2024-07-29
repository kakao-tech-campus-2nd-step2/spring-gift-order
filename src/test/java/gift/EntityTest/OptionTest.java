package gift.EntityTest;

import gift.domain.menu.Menu;
import gift.domain.other.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OptionTest {

    private Option option;

    @BeforeEach
    void setUp() {
        Menu menu = new Menu();
        option = new Option(1L, "Option", 100L, menu);
    }

    @Test
    void testSubtractValidValue() throws IllegalAccessException {
        option.subtract(10L);

        assertThat(option.getId()).isEqualTo(1L);
        assertThat(option.getName()).isEqualTo("Option");
        // This should be 90 after subtracting 10
        assertThat(option.getQuantity()).isEqualTo(90L);
    }

    @Test
    void testSubtractInvalidValueLow() {
        assertThatExceptionOfType(IllegalAccessException.class)
                .isThrownBy(() -> option.subtract(101L))
                .withMessage("옵션의 수량은 1이상이거나, 1억 이하여야 합니다.");
    }

}
