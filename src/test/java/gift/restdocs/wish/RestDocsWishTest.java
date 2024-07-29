package gift.restdocs.wish;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.config.LoginWebConfig;
import gift.controller.WishListApiController;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.paging.PagingService;
import gift.request.ProductAddRequest;
import gift.request.ProductUpdateRequest;
import gift.request.WishListRequest;
import gift.response.ProductResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.WishService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

@WebMvcTest(value = WishListApiController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsWishTest extends AbstractRestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private WishService wishService;
    @MockBean
    private PagingService pagingService;

    private String token = "{ACCESS_TOKEN}";


    @Test
    void getWishList() throws Exception {
        //given
        Long memberId = 1L;
        int page = 1;
        String sort = "id";
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
            Sort.by(Direction.ASC, sort));
        List<Product> products = new ArrayList<>();
        LongStream.range(1, 6)
            .forEach(i -> products.add(demoProduct(i)));

        List<ProductResponse> response = products.stream()
            .map(ProductResponse::createProductResponse)
            .toList();

        given(pagingService.makeWishPageRequest(any(int.class), any(String.class)))
            .willReturn(pageRequest);
        given(wishService.getPagedWishList(any(Long.class), any(PageRequest.class)))
            .willReturn(response);

        //when //then
        mockMvc.perform(get("/api/wishlist?page=" + page + "&sort=" + sort)
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-wish-test/get-wish-list",
                queryParameters(
                    parameterWithName("page").description("page number"),
                    parameterWithName("sort").description("sort option ex) id, name, quantity")
                )));
    }

    @Test
    void addWishList() throws Exception {
        //given
        Long memberId = 1L;
        Long productId = 1L;
        WishListRequest wishListRequest = new WishListRequest(productId);
        String content = objectMapper.writeValueAsString(wishListRequest);
        doNothing().when(wishService).
            addMyWish(any(Long.class), any(Long.class));

        //when //then
        mockMvc.perform(post("/api/wishlist")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void deleteWishList() throws Exception {
        //given
        Long memberId = 1L;
        Long productId = 1L;
        WishListRequest wishListRequest = new WishListRequest(productId);
        String content = objectMapper.writeValueAsString(wishListRequest);
        doNothing().when(wishService).
            deleteMyWish(any(Long.class), any(Long.class));

        //when //then
        mockMvc.perform(delete("/api/wishlist")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());

    }

    private ProductAddRequest demoAddRequest() {
        return new ProductAddRequest("상품1", 1000, "http://a.com",
            "교환권", "옵션A", 1);
    }

    private ProductUpdateRequest demoUpdateRequest(Long id) {
        return new ProductUpdateRequest(id, "수정된 상품명", 1500, "http://update.com",
            "수정된 카테고리명");
    }

    private Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션" + id, 1, product);
    }

    private Product demoProduct(Long id) {
        return new Product(id, "상품" + id, 1000, "http://a.com",
            new Category(1L, "교환권"));
    }


}
