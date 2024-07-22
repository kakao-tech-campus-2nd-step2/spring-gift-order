package gift.Menu;

import gift.domain.Category;
import gift.domain.Menu;
import gift.domain.Option;
import gift.repository.CategoryRepository;
import gift.repository.MenuRepository;
import gift.repository.OptionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("메뉴 FindById 테스트")
    void testFindById() {
        // Given
        Category category = new Category(null,"양식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option1 = new Option(null,"알리오올리오",3L);
        Option option2 = new Option(null,"토마토",4L);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Set<Option> options = new HashSet<Option>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타",3000,"naver.com",category,options);

        menu = menuRepository.save(menu);

        // When
        Optional<Menu> foundMenu = menuRepository.findById(menu.getId());

        // Then
        assertThat(foundMenu).isPresent();
        assertThat(foundMenu.get().getName()).isEqualTo("파스타");
        assertThat(foundMenu.get().getOptions()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴 FindAll 테스트")
    void testFindAll() {

        // Given
        Category category = new Category(null,"양식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option1 = new Option(null,"알리오올리오",3L);
        Option option2 = new Option(null,"토마토",4L);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Set<Option> options = new HashSet<Option>();
        options.add(option1);
        options.add(option2);

        Menu menu1 = new Menu("파스타",3000,"naver.com",category,options);

        Category category2 = new Category(null,"한식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option3 = new Option(null,"매운맛",3L);
        Option option4 = new Option(null,"순한맛",4L);
        optionRepository.save(option3);
        optionRepository.save(option4);

        Set<Option> options2 = new HashSet<Option>();
        options2.add(option3);
        options2.add(option4);

        Menu menu2 = new Menu("떡볶이",3000,"naver.com",category2,options2);

        menuRepository.save(menu1);
        menuRepository.save(menu2);

        // When
        Page<Menu> page = menuRepository.findAll(PageRequest.of(0, 10));

        // Then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting(Menu::getName).containsExactlyInAnyOrder("파스타", "떡볶이");
    }

    @Test
    @DisplayName("메뉴로 옵션 찾기 테스트")
    void testGetOptionsById() {
        // Given
        Category category = new Category(null,"양식",new LinkedList<Menu>());
        categoryRepository.save(category);

        Option option1 = new Option(null,"알리오올리오",3L);
        Option option2 = new Option(null,"토마토",4L);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Set<Option> options = new HashSet<Option>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타",3000,"naver.com",category,options);

        menu = menuRepository.save(menu);

        // When
        Set<Option> foundOptions = menuRepository.getOptionsByMenuId(menu.getId());

        // Then
        assertThat(foundOptions).hasSize(2);
        assertThat(foundOptions).extracting(Option::getName).containsExactlyInAnyOrder("알리오올리오", "토마토");
    }
}
