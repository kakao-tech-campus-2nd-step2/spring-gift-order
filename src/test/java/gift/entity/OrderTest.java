package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(new Option(), new User(), 30, LocalDateTime.now(), "testMessage");
    }

    @Test
    @DisplayName("생성자 테스트")
    void OrderConstructorTest() {
        Order newOrder = new Order(new Option(), new User(), 50, LocalDateTime.now(), "newTestMessage");

        assertThat(newOrder).isNotNull();
        assertThat(newOrder.getQuantity()).isEqualTo(50);
        assertThat(newOrder.getMessage()).isEqualTo("newTestMessage");
    }

    @Test
    @DisplayName("getTotalPrice 테스트")
    void getTotalPriceTest() {
        Product product = new Product("상품", 10000, "testImg.jpg", new Category());

        assertThat(order.getTotalPrice(product)).isEqualTo(10000*30);
    }
}
