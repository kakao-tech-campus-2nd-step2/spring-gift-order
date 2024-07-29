package gift.dto.response;

import java.util.List;

public record CategoryResponseDTO(Long id,
                                  String name,
                                  String color,
                                  String description,
                                  String imageUrl,
                                  List<String> productNames) {
}
