package gift.domain.other;

import gift.domain.menu.Menu;

public record WishListResponse(
        Long Id,
        Menu menu
) {
}
