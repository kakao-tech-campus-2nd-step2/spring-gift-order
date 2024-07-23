package gift;


import gift.dto.option.OptionQuantityDTO;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;

public class Test {
    @org.junit.jupiter.api.Test
    void save(){
        Category category = new Category("1");
        Product product = new Product("1",1,"1",category);
        Option option = new Option(product,"1");
        OptionQuantityDTO optionQuantityDTO = new OptionQuantityDTO(1,1,"1");
        Order order = new Order(optionQuantityDTO,option);

    }
}
