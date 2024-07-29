package gift.dto;

public class SendMessageRequest {
    private String bearerToken;
    private OrderRequest orderRequest;

    public SendMessageRequest(String bearerToken, OrderRequest orderRequest) {
        this.bearerToken = bearerToken;
        this.orderRequest = orderRequest;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public OrderRequest getOrderRequest() {
        return orderRequest;
    }

    public String getAccessToken() {
        return bearerToken.replace("Bearer ", "");
    }
}