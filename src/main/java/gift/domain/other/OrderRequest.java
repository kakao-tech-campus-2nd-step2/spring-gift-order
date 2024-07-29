package gift.domain.other;

public record OrderRequest(
        Long optionId,
        Long quantity,
        String message
) {
}
