package gift.repository;

import gift.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductOptionRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Test
    void deleteByProductIdAndOptionId() {
        // given
        Product product = productRepository.save(new Product(new ProductDTO("test", 123, "test.com", 1L)));
        Option option = optionRepository.save(new Option(new OptionDTO("test", 123)));
        productOptionRepository.save(new ProductOption(product, option, option.getName()));

        // when
        productOptionRepository.deleteByProductIdAndOptionId(product.getId(), option.getId());

        // then
        assertThat(productOptionRepository.findByProductId(product.getId()).size()).isEqualTo(0);
    }

    @Test
    void findByProductIdAndOptionId_존재x() {
        // given
        Product product = productRepository.save(new Product(new ProductDTO("test", 123, "test.com", 1L)));
        Option option = optionRepository.save(new Option(new OptionDTO("test", 123)));
        productOptionRepository.save(new ProductOption(product, option, option.getName()));

        // when
        Optional<ProductOption> expect = productOptionRepository.findByProductIdAndOptionId(product.getId(), option.getId()+1);

        // then
        assertThat(expect).isEmpty();
    }

    @Test
    void findByProductIdAndOptionId_존재O() {
        // given
        Product product = productRepository.save(new Product(new ProductDTO("test", 123, "test.com", 1L)));
        Option option = optionRepository.save(new Option(new OptionDTO("test", 123)));
        productOptionRepository.save(new ProductOption(product, option, option.getName()));

        // when
        Optional<ProductOption> expect = productOptionRepository.findByProductIdAndOptionId(product.getId(), option.getId());

        // then
        assertThat(expect).isPresent();
    }
}
