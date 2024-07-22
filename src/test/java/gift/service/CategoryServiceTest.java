package gift.service;

import gift.domain.Category;
import gift.dto.request.CategoryRequestDto;
import gift.dto.response.CategoryResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.NameDuplicationException;
import gift.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 저장 테스트")
    void 카테고리_저장_테스트(){
        //given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("상품권", "#0000");
        CategoryRequestDto inValidCategoryDto = new CategoryRequestDto("고기", "#0000");
        Category category = new Category(inValidCategoryDto.name(), inValidCategoryDto.color());

        given(categoryRepository.findCategoryByName(categoryRequestDto.name())).willReturn(Optional.empty());
        given(categoryRepository.findCategoryByName(inValidCategoryDto.name())).willReturn(Optional.of(category));

        //when
        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(categoryRequestDto);

        //then
        assertAll(
                () -> assertThat(categoryResponseDto.name()).isEqualTo(categoryRequestDto.name()),
                () -> assertThat(categoryResponseDto.color()).isEqualTo(categoryRequestDto.color()),
                () -> assertThatThrownBy(() -> categoryService.saveCategory(inValidCategoryDto))
                        .isInstanceOf(NameDuplicationException.class)
        );

    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void 카테고리_전체_조회_테스트(){
        //given
        Category category1 = new Category("상품권", "#0000");
        Category category2 = new Category("고기", "#0000");
        Category category3 = new Category("생선", "#0000");

        List<Category> categories = Arrays.asList(category1, category2, category3);

        given(categoryRepository.findAll()).willReturn(categories);

        //when
        List<CategoryResponseDto> categoryResponseDtos = categoryService.findAllCategories();

        //then
        assertAll(
                () -> assertThat(categoryResponseDtos.size()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("카테고리 단건 조회 테스트")
    void 카테고리_단건_조회_테스트(){
        //given
        Category category = new Category("상품권", "#0000");

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());

        //when
        CategoryResponseDto categoryResponseDto = categoryService.findOneCategoryById(1L);

        //then
        assertAll(
                () -> assertThat(categoryResponseDto.name()).isEqualTo(category.getName()),
                () -> assertThat(categoryResponseDto.color()).isEqualTo(category.getColor()),
                () -> assertThatThrownBy(() -> categoryService.findOneCategoryById(2L))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage("해당 카테고리는 존재하지 않습니다.")
        );
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void 카테고리_수정_테스트(){
        //given
        Category category = new Category("상품권", "#0000");
        Category inValidCategory = new Category("생선", "#0000");
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("고기", "#1234");
        CategoryRequestDto inValidCategoryRequestDto = new CategoryRequestDto("생선", "#1234");

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());
        given(categoryRepository.findCategoryByName(categoryRequestDto.name())).willReturn(Optional.empty());
        given(categoryRepository.findCategoryByName(inValidCategoryRequestDto.name())).willReturn(Optional.of(inValidCategory));

        //when
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(1L, categoryRequestDto);

        //then
        assertAll(
                () -> assertThat(categoryResponseDto.name()).isEqualTo("고기"),
                () -> assertThat(categoryResponseDto.color()).isEqualTo("#1234"),
                () -> assertThatThrownBy(() -> categoryService.updateCategory(2L, categoryRequestDto))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage("해당 카테고리는 존재하지 않습니다."),
                () -> assertThatThrownBy(() -> categoryService.updateCategory(1L, inValidCategoryRequestDto))
                        .isInstanceOf(NameDuplicationException.class)
        );
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void 카테고리_삭제_테스트(){
        //given
        Category category = new Category("상품권", "#0000");

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.findById(2L)).willReturn(Optional.empty());

        //when
        CategoryResponseDto categoryResponseDto = categoryService.deleteCategory(1L);

        //then
        assertAll(
                () -> assertThat(categoryResponseDto.name()).isEqualTo(category.getName()),
                () -> assertThat(categoryResponseDto.color()).isEqualTo(category.getColor()),
                () -> assertThatThrownBy(() -> categoryService.deleteCategory(2L))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage("해당 카테고리는 존재하지 않습니다.")
        );
    }

}