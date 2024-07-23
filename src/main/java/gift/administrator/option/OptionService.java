package gift.administrator.option;

import gift.administrator.product.Product;
import java.util.List;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public List<OptionDTO> getAllOptionsByProductId(long productId) {
        return optionRepository.findAllByProductId(productId).stream().map(OptionDTO::fromOption)
            .toList();
    }

    public List<OptionDTO> getAllOptions() {
        return optionRepository.findAll().stream().map(OptionDTO::fromOption).toList();
    }

    public List<OptionDTO> getAllOptionsByOptionId(List<Long> list) {
        return optionRepository.findAllById(list).stream().map(OptionDTO::fromOption).toList();
    }

    public boolean existsByOptionIdAndProductId(long optionId, long productId) {
        return optionRepository.existsByIdAndProductId(optionId, productId);
    }

    public void existsByNameSameProductIdNotOptionId(String name, long productId, long optionId) {
        if (optionRepository.existsByNameAndProductIdAndIdNot(name, productId, optionId)) {
            throw new IllegalArgumentException("같은 상품 내에서 동일한 이름의 옵션은 불가합니다.");
        }
    }

    public int countAllOptionsByProductIdFromOptionId(long optionId) throws NotFoundException {
        long productId = findOptionById(optionId).getProductId();
        return optionRepository.countAllByProductId(productId);
    }

    public OptionDTO updateOption(long optionId, OptionDTO optionDTO) throws NotFoundException {
        Option option = optionRepository.findById(optionId).orElseThrow(NotFoundException::new);
        option.update(optionDTO.getName(), optionDTO.getQuantity());
        return OptionDTO.fromOption(optionRepository.save(option));
    }

    public OptionDTO findOptionById(long optionId) throws NotFoundException {
        return OptionDTO.fromOption(
            optionRepository.findById(optionId).orElseThrow(NotFoundException::new));
    }

    public Option subtractOptionQuantity(long optionId, int quantity) throws NotFoundException {
        Option option = optionRepository.findById(optionId).orElseThrow(NotFoundException::new);
        if (option.getQuantity() < quantity) {
            throw new IllegalArgumentException("옵션의 수량이 부족합니다.");
        }
        option.subtract(quantity);
        return option;
    }

    public void deleteAllWhenUpdatingProduct(List<Option> options, Product product) {
        product.getOptions().removeAll(options);
        optionRepository.deleteAll(options);
    }

    public void deleteOptionByOptionId(long optionId) throws NotFoundException {
        if (!optionRepository.existsById(optionId)) {
            throw new IllegalArgumentException("없는 아이디입니다.");
        }
        Option option = optionRepository.findById(optionId).orElseThrow(NotFoundException::new);
        option.getProduct().removeOption(option);
        optionRepository.deleteById(option.getId());
    }
}
