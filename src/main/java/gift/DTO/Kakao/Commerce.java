package gift.DTO.Kakao;

public class Commerce {
    private String product_name;
    private int regular_price;

    public Commerce(String product_name, int regular_price) {
        this.product_name = product_name;
        this.regular_price = regular_price;
    }

    public String getProduct_name(){
        return product_name;
    }

    public int getRegular_price() {
        return regular_price;
    }
}
