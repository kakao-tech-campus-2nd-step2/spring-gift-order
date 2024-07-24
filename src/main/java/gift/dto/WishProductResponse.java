package gift.dto;

public record WishProductResponse(Long id, ProductBasicInformation productBasicInformation, Integer quantity) {
    public static WishProductResponse of(Long id, ProductBasicInformation productBasicInformation, Integer quantity) {
        return new WishProductResponse(id, productBasicInformation, quantity);
    }
}
