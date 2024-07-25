package gift.controller;

import gift.dto.ProductDto;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductDto product1;
    private ProductDto product2;

    @BeforeEach
    void setUp() {
        product1 = new ProductDto(1L, "Product 1", 100, "img1.jpg", 1L);
        product2 = new ProductDto(2L, "Product 2", 200, "img2.jpg", 2L);
    }

    @Test
    void listProducts() throws Exception {
        given(productService.findAll()).willReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(2)));
    }

    @Test
    void viewProduct() throws Exception {
        given(productService.findById(1L)).willReturn(Optional.of(product1));

        mockMvc.perform(get("/admin/product/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product1));
    }

    @Test
    void viewProductNotFound() throws Exception {
        given(productService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/admin/product/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void showAddProductForm() throws Exception {
        mockMvc.perform(get("/admin/product/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void addProduct() throws Exception {
        mockMvc.perform(post("/admin/product/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "New Product")
                        .param("price", "300")
                        .param("imgUrl", "img3.jpg")
                        .param("categoryId", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        Mockito.verify(productService).save(any(ProductDto.class));
    }

    @Test
    void showEditProductForm() throws Exception {
        given(productService.findById(1L)).willReturn(Optional.of(product1));

        mockMvc.perform(get("/admin/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", product1));
    }

    @Test
    void editProduct() throws Exception {
        mockMvc.perform(post("/admin/product/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Product")
                        .param("price", "400")
                        .param("imgUrl", "updated_img.jpg")
                        .param("categoryId", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        Mockito.verify(productService).update(eq(1L), any(ProductDto.class));
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(post("/admin/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        Mockito.verify(productService).delete(1L);
    }
}