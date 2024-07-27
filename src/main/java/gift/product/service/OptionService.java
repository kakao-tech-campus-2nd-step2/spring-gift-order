package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.dto.OptionDTO;
import gift.product.exception.InvalidIdException;
import gift.product.model.Option;
import gift.product.model.Product;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.validation.OptionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final OptionValidation optionValidation;

    @Autowired
    public OptionService(
        OptionRepository optionRepository,
        ProductRepository productRepository,
        OptionValidation optionValidation
    ) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
        this.optionValidation = optionValidation;
    }

    public Page<Option> getAllOptions(Long id, Pageable pageable) {
        System.out.println("[OptionService] getAllOptions()");
        return optionRepository.findAllByProductId(id, pageable);
    }

    public Option registerOption(Long productId, OptionDTO optionDTO) {
        System.out.println("[OptionService] registerOption()");
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        Option option = optionDTO.convertToDomain(product);
        optionValidation.register(option);
        return optionRepository.save(option);
    }

    public Option updateOption(Long id, OptionDTO optionDTO) {
        System.out.println("[OptionService] updateOption()");
        Product product = optionRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID))
            .getProduct();
        Option option = optionDTO.convertToDomain(id, product);
        optionValidation.update(option);
        return optionRepository.save(option);
    }

    public void deleteOption(Long id, Long productId) {
        System.out.println("[OptionService] deleteOption()");
        optionValidation.delete(id, productId);
        optionRepository.deleteById(id);
    }

    public Option findById(Long id) {
        System.out.println("[OptionService] findById()");
        return optionRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }
}
