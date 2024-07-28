package gift.database;

import static org.junit.jupiter.api.Assertions.*;

import gift.category.entity.Category;
import gift.category.facadeRepository.CategoryFacadeRepository;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.product.entity.Product;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class categoryFacadeRepositoryTest {

    @Autowired
    private CategoryFacadeRepository categoryFacadeRepository;

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Test
    @DisplayName("카테고리가 존재하지 않는데 상품이 등록된 카테고리 저장 시도 시 실패")
    void test1(){
        // given
        Category category = new Category(null, "test", "red", "test", "test");
        category.addProduct(new Product(null, "test", 123, "abc"));

        // when

        // then
        assertThrows(GiftNotFoundException.class, () -> categoryFacadeRepository.save(category));
    }
}