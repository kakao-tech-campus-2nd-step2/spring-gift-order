package gift.service;

import gift.common.annotation.RedissonLock;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final ProductService productService;

    public RedisService(ProductService productService) {
        this.productService = productService;
    }

    @RedissonLock(value = "#id + ':' + #optionId")
    public int subtractQuantityRedisLock(Long id, Long optionId, int amount) {
        return productService.subtractQuantity(id, optionId, amount);
    }
}
