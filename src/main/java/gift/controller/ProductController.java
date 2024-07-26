package gift.controller;


import gift.dto.product.ModifyProductDTO;
import gift.dto.product.SaveProductDTO;
import gift.dto.product.ShowProductDTO;
import gift.entity.Product;
import gift.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/api/products")
    public Page<ShowProductDTO> getProducts(@RequestParam(value = "page", defaultValue = "0") int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 2, Sort.by(Sort.Direction.ASC, "id"));
        return productService.getAllProducts(pageable);

    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    public String addProduct(@RequestBody SaveProductDTO product) {
        productService.saveProduct(product);
        return "redirect:api/products";
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/products/{Id}")
    public String deleteProduct(@PathVariable int Id) {
        productService.deleteProduct(Id);
        return "redirect:api/products";
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/products")
    public String updateProduct(@RequestBody ModifyProductDTO product) {
        productService.updateProduct(product);
        return "redirect:api/products";
    }


    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/api/product/{id}")
    public String getProduct(@PathVariable int Id) {
        String product = productService.getProductByID(Id);
        return product;
    }
}

