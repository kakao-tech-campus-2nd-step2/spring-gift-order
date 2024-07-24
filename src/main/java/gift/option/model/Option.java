package gift.option.model;

import gift.common.exception.OptionException;
import gift.option.OptionErrorCode;
import gift.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {
    }

    public Option(String name, Integer quantity, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.product = product;
        product.addOption(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void updateInfo(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void subtract(Integer quantity) throws OptionException {
        if (this.quantity < quantity) {
            throw new OptionException(OptionErrorCode.NOT_ENOUGH_QUANTITY);
        }
        this.quantity = this.quantity - quantity;
    }

    public static class Validator {

        public static void validateName(List<Option> optionList, Option newOption) throws OptionException {
            optionList.add(newOption);
            validateDuplicated(optionList);
        }

        public static void validateOptionCount(List<Option> options) throws OptionException {
            if (options.size() <= 1) {
                throw new OptionException(OptionErrorCode.OPTION_COUNT_ONE);
            }
        }

        public static void validateDuplicated(List<Option> optionList) throws OptionException {
            List<String> optionNameList = getOptionNames(optionList);
            Set<String> optionNameSet = new HashSet<>(optionNameList);
            if(optionNameList.size() != optionNameSet.size()) {
                throw new OptionException(OptionErrorCode.NAME_DUPLICATED);
            }
        }

        private static List<String> getOptionNames(List<Option> optionList) {
            return optionList.stream().map(Option::getName).toList();
        }
    }
}
