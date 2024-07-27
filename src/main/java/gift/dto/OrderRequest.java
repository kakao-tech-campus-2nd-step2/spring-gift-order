package gift.dto;

public record OrderRequest(Long optionId, int quantity, String message) {
}
