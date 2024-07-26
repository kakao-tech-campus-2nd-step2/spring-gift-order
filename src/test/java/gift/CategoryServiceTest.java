package gift;


import gift.entity.Category;
import gift.repository.CategoryRepository;
import gift.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
public class CategoryServiceTest {

    private CategoryService categoryService;

    private CategoryRepository categoryRepository;

    @Test
    void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
                categoryRepository.findCategoryByName("식품"),
                categoryRepository.findCategoryByName("패션")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("식품", result.get(0).getName());
        assertEquals("패션", result.get(1).getName());
    }
}
//
//Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
//
//java.lang.NullPointerException: Cannot invoke "gift.repository.CategoryRepository.findCategoryByName(String)" because "this.categoryRepository" is null
//
//at gift.CategoryServiceTest.testGetAllCategories(CategoryServiceTest.java:26)
//at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
