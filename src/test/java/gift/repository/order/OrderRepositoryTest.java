package gift.repository.order;

import gift.model.category.Category;
import gift.model.gift.Gift;
import gift.model.option.Option;
import gift.model.order.Order;
import gift.repository.gift.GiftRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GiftRepository giftRepository;

    @Test
    @DisplayName("주문정보가 잘 저장되는지 확인")
    void testSaveOrder() {
        //given
        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option("testOption", 1);
        List<Option> option = Arrays.asList(option1);
        Gift gift = new Gift("test", 1000, "abc.jpg", category, option);
        giftRepository.save(gift);

        Order order = new Order(gift.getOptions().get(0),5,"testMessage");

        //when
        Order savedOrder = orderRepository.save(order);

        //then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder).isSameAs(order);
    }
}