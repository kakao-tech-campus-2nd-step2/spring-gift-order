package gift.restdocs.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.auth.JwtTokenProvider;
import gift.config.LoginWebConfig;
import gift.controller.ProductApiController;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.request.ProductAddRequest;
import gift.response.OptionResponse;
import gift.response.ProductOptionsResponse;
import gift.response.ProductResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.OptionsService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = ProductApiController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsGetProduct extends AbstractRestDocsTest {

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private ProductService productService;
    @MockBean
    private OptionsService optionsService;

    private String token;

    @Test
    void getAllProducts() throws Exception {
        //given
        token = "{ACCESS_TOKEN}";
        Product product = demoProduct(1L);

        List<Product> products = new ArrayList<>();
        LongStream.range(1,6)
            .forEach(i -> products.add(demoProduct(i)));

        List<ProductResponse> response = products.stream()
            .map(ProductResponse::new)
            .toList();

        given(productService.getAllProducts())
            .willReturn(response);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products", product.getId())
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void getProductWithAllOptions() throws Exception {
        //given
        token = "{ACCESS_TOKEN}";
        Product product = demoProduct(1L);
        Options option = demoOptions(1L, product);
        Options option2 = demoOptions(2L, product);
        List<OptionResponse> options = new ArrayList<>();
        options.add(new OptionResponse(option.getId(), option.getName(), option.getQuantity()));
        options.add(new OptionResponse(option2.getId(), option2.getName(), option2.getQuantity()));

        ProductResponse productResponse = new ProductResponse(product);
        ProductOptionsResponse response = new ProductOptionsResponse(productResponse, options);
        given(productService.getProduct(any(Long.class)))
            .willReturn(product);
        given(optionsService.getAllProductOptions(any(Product.class)))
            .willReturn(response);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/{id}/all", product.getId())
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void getProductWithOption() throws Exception {
        //given
        Long optionId = 1L;
        Product product = demoProduct(1L);
        List<OptionResponse> options = new ArrayList<>();
        ProductResponse productResponse = new ProductResponse(product);
        ProductOptionsResponse response = new ProductOptionsResponse(productResponse, options);

        given(productService.getProduct(any(Long.class)))
            .willReturn(product);
        given(optionsService.getProductOption(any(Product.class), any(Long.class)))
            .willReturn(response);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/products/{id}", product.getId())
                    .param("option_id", String.valueOf(optionId))
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());

    }





    private ProductAddRequest demoAddRequest() {
        return new ProductAddRequest("상품1", 1000, "http://a.com",
            "교환권", "옵션A", 1);
    }

    private static Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션" + id, 1, product);
    }

    private static Product demoProduct(Long id) {
        return new Product(id, "상품"+id, 1000, "http://a.com",
            new Category(1L, "교환권"));
    }



}
