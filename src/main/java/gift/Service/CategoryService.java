package gift.Service;

import gift.Exception.CategoryNotFoundException;
import gift.Model.Category;
import gift.DTO.RequestCategoryDTO;
import gift.DTO.ResponseCategoryDTO;
import gift.Repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category addCategory(RequestCategoryDTO requestCategoryDTO) {
        Category category = new Category(requestCategoryDTO.name(), requestCategoryDTO.color(), requestCategoryDTO.imageUrl(), requestCategoryDTO.description());
        return categoryRepository.save(category);
    }
    @Transactional(readOnly = true)
    public List<ResponseCategoryDTO> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(it->new ResponseCategoryDTO(
                        it.getId(),
                        it.getName(),
                        it.getColor(),
                        it.getImageUrl(),
                        it.getDescription()))
                .toList();
    }

    @Transactional
    public void editCategory(RequestCategoryDTO requestCategoryDTO) {
        Category category = categoryRepository.findByName(requestCategoryDTO.name())
                .orElseThrow(()-> new CategoryNotFoundException("매칭되는 카테고리가 없습니다"));
        category.update(requestCategoryDTO.name(), requestCategoryDTO.color(), requestCategoryDTO.imageUrl(), requestCategoryDTO.description());;
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("매칭되는 카테고리가 없습니다"));
        categoryRepository.deleteById(categoryId);
    }
}
