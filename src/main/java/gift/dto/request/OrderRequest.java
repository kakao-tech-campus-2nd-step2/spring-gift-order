package gift.dto.request;

public record OrderRequest(
        Long optionId,
        Integer quantity,
        String message
) {
}
