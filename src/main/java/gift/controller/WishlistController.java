package gift.controller;

import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.service.WishlistService;
import gift.util.KakaoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wishes")
public class WishlistController {

    private final WishlistService wishlistService;
    private final KakaoUtil kakaoUtil;

    @Autowired
    public WishlistController(WishlistService wishlistService, KakaoUtil kakaoUtil) {
        this.wishlistService = wishlistService;
        this.kakaoUtil = kakaoUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(token.replace("Bearer ", ""));
        String userRole = (String) userInfo.get("role");
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        Long memberId = ((Number) userInfo.get("id")).longValue();

        Long productId = Long.valueOf(requestBody.get("productId").toString());
        int productNumber = Integer.parseInt(requestBody.get("productNumber").toString());

        Member member = new Member();
        member.setId(memberId);
        Product product = new Product();
        product.setId(productId);

        Wish wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
        wish.setProductNumber(productNumber);

        Wish addedWish = wishlistService.addProduct(wish);
        return ResponseEntity.ok(new WishResponse(addedWish.getId(), addedWish.getProduct().getId(), addedWish.getProduct().getName(), addedWish.getProductNumber()));
    }

    @GetMapping("/items")
    public String getItems(@RequestHeader("Authorization") String token,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "id") String sortBy,
                           @RequestParam(defaultValue = "asc") String direction,
                           Model model) {
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(token.replace("Bearer ", ""));
        String userRole = (String) userInfo.get("role");
        if (!"USER".equals(userRole)) {
            return "redirect:/unauthorized";
        }

        Long memberId = ((Number) userInfo.get("id")).longValue();

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<WishResponse> wishPage = wishlistService.getWishesByMemberId(memberId, pageRequest);

        model.addAttribute("wishPage", wishPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "wishlist";
    }

    @GetMapping("/item-details/{productId}")
    public ResponseEntity<?> getProductDetails(@RequestHeader("Authorization") String token, @PathVariable Long productId) {
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(token.replace("Bearer ", ""));
        String userRole = (String) userInfo.get("role");
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        try {
            Product product = wishlistService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProductNumber(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody int productNumber) {
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(token.replace("Bearer ", ""));
        String userRole = (String) userInfo.get("role");
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        try {
            wishlistService.updateProductNumber(id, productNumber);
            return ResponseEntity.ok("Successfully updated product quantity.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(token.replace("Bearer ", ""));
        String userRole = (String) userInfo.get("role");
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Forbidden");
        }

        try {
            wishlistService.deleteItem(id);
            return ResponseEntity.ok("Successfully deleted product.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found.");
        }
    }
}
