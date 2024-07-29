package gift.domain.other;

import gift.domain.menu.Menu;

public record OptionResponse(
        Long id,
        String name,
        Long quantity,
        Menu menu
) {
}
