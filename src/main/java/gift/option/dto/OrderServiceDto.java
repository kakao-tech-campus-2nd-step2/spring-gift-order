package gift.option.dto;

import gift.option.domain.Option;
import gift.option.domain.OptionCount;
import gift.option.domain.OptionName;
import gift.product.domain.Product;

public record OrderServiceDto(Long id, OptionName name, OptionCount count, Long productId) {
    public Option toOption(Product product) {
        return new Option(id, name, count, product);
    }
}
