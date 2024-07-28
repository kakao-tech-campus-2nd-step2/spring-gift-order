package gift.service;

import gift.entity.*;
import gift.exception.ResourceNotFoundException;
import gift.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductWishlistRepository productWishlistRepository;
    private final CategoryService categoryService;
    private final ProductOptionRepository productOptionRepository;
    private final OptionRepository optionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductWishlistRepository productWishlistRepository, CategoryService categoryService, ProductOptionRepository productOptionRepository, OptionRepository optionRepository, UserService userService, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productWishlistRepository = productWishlistRepository;
        this.categoryService = categoryService;
        this.productOptionRepository = productOptionRepository;
        this.optionRepository = optionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<Wishlist> getProductWishlist(Long productId) {
        List<ProductWishlist> productWishlists = productWishlistRepository.findByProductId(productId);
        List<Wishlist> wishlists = productWishlists.stream()
                .map(productWishlist -> productWishlist.getWishlist())
                .collect(Collectors.toList());
        return wishlists;
    }

    @Transactional
    public Product save(ProductDTO productDTO, String email) {
        User user = userService.findOne(email);

        Product product = productRepository.save(new Product(productDTO, user));

        Option defaultOption = optionRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: 1L"));

        productOptionRepository.save(new ProductOption(product, defaultOption, defaultOption.getName()));

        return product;
    }

    @Transactional
    public Product update(Long id, ProductDTO productDTO, String email) {
        if (!productMatchesUser(id, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Product product = findById(id);
        if (product.getCategoryid() != productDTO.getCategoryid()) {
            Category category = categoryService.findById(productDTO.getCategoryid());
            productDTO.setCategoryid(category.getId());
        }

        product.updateProduct(productDTO);

        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id, String email) {
        if (!productMatchesUser(id, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Product product = findById(id);
        product.setUser(null);

        productRepository.delete(product);
    }

    // option
    public List<Option> getOptions(Long id) {
        List<ProductOption> productOptions = productOptionRepository.findByProductId(id);
        return productOptions.stream()
                .map(productOption -> productOption.getOption())
                .collect(Collectors.toList());
    }

    @Transactional
    public void addProductOption(Long productId, Long optionId, String email) {
        if (!productMatchesUser(productId, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + optionId));
        Product product = findById(productId);
        productOptionRepository.save(new ProductOption(product, option, option.getName()));
    }

    @Transactional
    public void deleteProductOption(Long productId, Long optionId, String email) {
        if (!productMatchesUser(productId, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        // 옵션이 하나 이상인지 확인 로직
        if (productOptionRepository.findByProductId(productId).size() <= 1) {
            throw new IllegalArgumentException("Option should have at least one product option");
        }

        productOptionRepository.deleteByProductIdAndOptionId(productId, optionId);
    }

    public boolean productMatchesUser(Long id, String email) {
        User user = userService.findOne(email);
        Product product = findById(id);
        return user.getId() == product.getUser().getId();
    }
}
