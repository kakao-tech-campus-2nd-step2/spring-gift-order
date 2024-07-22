package gift.service;

import gift.entity.Option;
import gift.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class OptionFacadeService {

    private final OptionService optionService;
    private final ProductService productService;

    public OptionFacadeService(OptionService optionService, ProductService productService) {
        this.optionService = optionService;
        this.productService = productService;
    }

    public Product findProductById(Long id) {
        return productService.getProductById(id);
    }

    public void addOption(Option option) {
        optionService.addOption(option);
    }

    public void updateOption(Option option, Long id) {
        optionService.updateOption(option, id);
    }

    public void deleteOption(Long id) {
        optionService.deleteOption(id);
    }


}
