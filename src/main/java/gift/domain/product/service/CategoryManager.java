package gift.domain.product.service;

import gift.domain.product.dto.CategoryResponse;
import gift.domain.product.entity.Category;
import gift.domain.product.repository.CategoryJpaRepository;
import gift.exception.InvalidCategoryInfoException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CategoryManager {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryManager(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    public List<CategoryResponse> readAll() {
        List<Category> categories = categoryJpaRepository.findAll();
        return categories.stream().map(CategoryResponse::from).toList();
    }

    public Category readById(long categoryId) {
        return categoryJpaRepository.findById(categoryId)
            .orElseThrow(() -> new InvalidCategoryInfoException("error.invalid.category.id"));
    }
}
