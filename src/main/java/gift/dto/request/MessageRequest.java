package gift.dto.request;

import gift.dto.response.OrderResponse;
import gift.entity.Product;

public class MessageRequest {
    
    private String accessToken;
    private OrderResponse orderResponse;
    private Product product;

    public MessageRequest(String accessToken, OrderResponse orderResponse, Product product){
        this.accessToken = accessToken;
        this.orderResponse = orderResponse;
        this.product = product;
    }
    
    public String getAccessToken(){
        return accessToken;
    }

    public OrderResponse getOrderResponse(){
        return orderResponse;
    }

    public Product getProduct(){
        return product;
    }
}
