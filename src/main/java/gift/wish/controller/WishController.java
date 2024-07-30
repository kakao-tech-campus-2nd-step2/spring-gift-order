package gift.wish.controller;

import gift.annotation.LoginUser;
import gift.config.PageConfig;
import gift.user.entity.User;
import gift.wish.dto.request.CreateWishRequest;
import gift.wish.dto.request.UpdateWishRequest;
import gift.wish.dto.response.WishResponse;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<Page<WishResponse>> getWishes(
        @LoginUser User user,
        @PageableDefault(
            size = PageConfig.PAGE_PER_COUNT,
            sort = PageConfig.SORT_STANDARD,
            direction = Direction.DESC
        ) Pageable pageable
    ) {
        return ResponseEntity.ok(wishService.getWishes(user.getId(), pageable));
    }

    @PostMapping
    public ResponseEntity<WishResponse> addWish(
        @LoginUser User user, @RequestBody @Valid CreateWishRequest request
    ) {
        WishResponse response = wishService.createWish(user.getId(), request);
        URI location = UriComponentsBuilder.fromPath("/api/wishes/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping
    public ResponseEntity<Void> updateWishes(
        @LoginUser User user, @RequestBody List<UpdateWishRequest> requests
    ) {
        wishService.updateWishes(requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"id"})
    public ResponseEntity<Void> deleteWishes(
        @LoginUser User user, @RequestParam Long id
    ) {
        wishService.deleteWish(id);
        return ResponseEntity.ok().build();
    }
}
