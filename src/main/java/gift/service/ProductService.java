package gift.service;

import gift.database.repository.JpaCategoryRepository;
import gift.database.repository.JpaProductRepository;
import gift.dto.ProductDTO;
import gift.exceptionAdvisor.exceptions.GiftBadRequestException;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.model.Category;
import gift.model.Product;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private final JpaProductRepository jpaProductRepository;

    private final JpaCategoryRepository jpaCategoryRepository;

    public ProductService(JpaProductRepository jpaProductRepository,
        JpaCategoryRepository jpaCategoryRepository) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    public List<ProductDTO> readAll() {
        return jpaProductRepository.findAll().stream().map(
            product -> new ProductDTO(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(), product.getCategory().getId())).toList();
    }

    public void create(ProductDTO dto) {
        checkKakao(dto.getName());
        Product product = new Product(null, dto.getName(), dto.getPrice(), dto.getImageUrl());
        jpaProductRepository.save(product);

        Category category = checkCategory(dto.getCategoryId());
        product.updateCategory(category);
    }


    public void update(long id, ProductDTO dto) {
        jpaProductRepository.findById(id).orElseThrow(()->new GiftNotFoundException("상품이 존재하지 않습니다."));

        Product product = new Product(id, dto.getName(), dto.getPrice(), dto.getImageUrl());
        Category category = checkCategory(dto.getCategoryId());

        product.updateCategory(category);
        jpaProductRepository.save(product);
    }

    public void delete(long id) {
        jpaProductRepository.deleteById(id);
    }

    private void checkKakao(String productName) {
        if (productName.contains("카카오")) {
            throw new GiftBadRequestException("카카오 문구는 MD 협의 이후 사용할 수 있습니다.");
        }
    }

    public List<ProductDTO> readProduct(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return jpaProductRepository.findAll(pageable).stream().map(
            product -> new ProductDTO(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl())).toList();
    }

    // private //

    private Product getProduct(long id) {
        var prod = jpaProductRepository.findById(id).orElseThrow(()-> new GiftNotFoundException("상품이 존재하지않습니다."));
        checkKakao(prod.getName());
        return prod;
    }

    private Category checkCategory(long id) {
        return jpaCategoryRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("카테고리가 존재하지않습니다."));
    }
}
