package gift.service;

import gift.exception.ValueNotFoundException;
import gift.model.product.Category;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private Category findCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).
                orElseThrow(() -> new ValueNotFoundException("Category not exists in Database"));
    }
    public synchronized Category findOrSaveCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
    }
}
