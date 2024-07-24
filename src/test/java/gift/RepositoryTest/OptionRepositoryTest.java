package gift.RepositoryTest;

import gift.domain.Option;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("옵션 저장 테스트")
    void testSaveOption() {
        Option option = new Option(null, "검정색", 100L);
        option = optionRepository.save(option);

        assertThat(option).isNotNull();
    }

    @Test
    @DisplayName("옵션 findById 테스트")
    void testFindOptionById() {
        Option option = new Option(null, "검정색", 50L);
        option = optionRepository.save(option);

        Optional<Option> foundOption = optionRepository.findById(option.getId());
        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("검정색");
    }

    @Test
    @DisplayName("옵션 삭제 테스트")
    void testDeleteOption() {
        Option option = new Option(null, "검정색", 30L);
        option = optionRepository.save(option);

        optionRepository.deleteById(option.getId());

        Optional<Option> deletedOption = optionRepository.findById(option.getId());
        assertThat(deletedOption).isNotPresent();
    }

    @Test
    @DisplayName("옵션 동일성 테스트")
    void testEquals() {
        Option option1 = new Option(null, "검정색", 50L);
        optionRepository.save(option1);

        Option option2 = new Option(null, "검정색", 60L);
        optionRepository.save(option2);

        Set<Option> optionSet = new HashSet<>();
        optionSet.add(option1);
        optionSet.add(option2);
        assertThat(optionSet).hasSize(1);
    }
}
