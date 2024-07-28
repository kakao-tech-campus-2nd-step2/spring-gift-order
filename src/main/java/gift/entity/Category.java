package gift.entity;

import gift.dto.category.CategoryDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @OneToMany(mappedBy = "category")
    List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    public int getId() {
        return id;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void deleteProduct(Product product) {
        this.products.remove(product);
    }

    public void updateCategoryName(String name) {
        this.name = name;
    }
    public CategoryDTO toDTO(){
        return new CategoryDTO(this.id,this.name);
    }
}
