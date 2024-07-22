package gift.service;

import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("옵션 삭제")
    void deleteOption() {
        // given
        Product product = new Product(1L, "Product", 1000, "1.img", null);
        Option option1 = new Option(1L, "option1", 5, product);
        Option option2 = new Option(2L, "deleteOption", 5, product);
        List<Option> options = Arrays.asList(option1, option2);
        given(optionRepository.findAllByProduct_Id(1L)).willReturn(options);
        given(optionRepository.findById(any())).willReturn(Optional.of(option2));
        long beforeSize = option1.getQuantity();
        // when
        optionService.deleteOption("deleteOption", 1L);
        // Then
        then(optionRepository).should().deleteByName("deleteOption");
        then(optionRepository).should().updateQuantityByProductId(1L, -1);
    }

    @Test
    @DisplayName("옵션 추가")
    void addOption() {
        // given
        Product product = new Product(1L, "Product", 1000, "1.img", null);
        Option option1 = new Option(1L, "Option1", 5, product);
        List<Option> options = Arrays.asList(option1);
        given(optionRepository.findAll()).willReturn(options);
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        // When
        optionService.addOption("newOption", 1L);
        // Then
        then(optionRepository).should().save(argThat(option -> "newOption".equals(option.getName())));
        then(optionRepository).should().updateQuantityByProductId(1L, 1);
    }

    @Test
    @DisplayName("옵션 수정")
    void updateOption() {
        // given
        Product product = new Product(1L, "Product", 1000, "1.img", null);
        Option oldOption = new Option(1L, "oldName", 5, product);
        Option updatedOption = new Option(1L, "newName", 5, product);
        List<Option> options = Arrays.asList(oldOption, updatedOption);
        given(optionRepository.findByName("oldName")).willReturn(Optional.of(oldOption));
        given(optionRepository.findAllByProduct_Id(1L)).willReturn(options);
        // When
        optionService.updateOption("oldName", "newName", 1L);
        // Then
        then(optionRepository).should().save(argThat(option -> "newName".equals(option.getName())));
    }

    @Test
    @DisplayName("옵션 n개 삭제")
    void removeOption() {
        // given
        Product product = new Product(1L, "Product", 1000, "1.img", null);
        Option option1 = new Option(1L, "option1", 5, product);
        Option option2 = new Option(2L, "option2", 5, product);
        List<Option> options = Arrays.asList(option1, option2);
        given(optionRepository.findAllByProduct_Id(1L)).willReturn(options);
        // When
        optionService.removeOption(1L, 1);
        // Then
        then(optionRepository).should().deleteById(1L);
        then(optionRepository).should().updateQuantityByProductId(1L, -1);
    }
}