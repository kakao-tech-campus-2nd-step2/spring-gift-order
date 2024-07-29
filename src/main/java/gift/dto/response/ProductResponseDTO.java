package gift.dto.response;

public record ProductResponseDTO(Long id,
                                 String name,
                                 int price,
                                 String imageUrl,
                                 String categoryName) {
}
