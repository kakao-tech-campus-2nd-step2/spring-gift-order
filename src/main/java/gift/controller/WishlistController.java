package gift.controller;

import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.service.MemberService;
import gift.service.WishlistService;
import gift.util.KakaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/wishes")
public class WishlistController {

    private final WishlistService wishlistService;
    private final KakaoUtil kakaoUtil;
    private final MemberService memberService;

    @Autowired
    public WishlistController(WishlistService wishlistService, KakaoUtil kakaoUtil, MemberService memberService) {
        this.wishlistService = wishlistService;
        this.kakaoUtil = kakaoUtil;
        this.memberService = memberService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(HttpSession session, @RequestBody Map<String, Object> requestBody) {
        String accessToken = (String) session.getAttribute("accessToken");
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(accessToken);
        String kakaoId = String.valueOf(userInfo.get("id"));

        Member member = memberService.findByKakaoId(kakaoId).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Long productId = Long.valueOf(requestBody.get("productId").toString());
        int productNumber = Integer.parseInt(requestBody.get("productNumber").toString());

        Wish wish = new Wish();
        wish.setMember(member);
        Product product = new Product();
        product.setId(productId);
        wish.setProduct(product);
        wish.setProductNumber(productNumber);

        Wish addedWish = wishlistService.addProduct(wish);
        return ResponseEntity.ok(new WishResponse(addedWish.getId(), addedWish.getProduct().getId(), addedWish.getProduct().getName(), addedWish.getProductNumber()));
    }

    @GetMapping("/items")
    public String getItems(HttpSession session,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "id") String sortBy,
                           @RequestParam(defaultValue = "asc") String direction,
                           Model model) {
        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken == null) {
            return "redirect:/members/login";
        }

        // KakaoUtil을 사용하여 사용자 정보 확인
        Map<String, Object> userInfo = kakaoUtil.getUserInfo(accessToken);
        if (userInfo == null || userInfo.get("id") == null) {
            return "redirect:/members/login";
        }

        String kakaoId = String.valueOf(userInfo.get("id"));
        Member member = memberService.findByKakaoId(kakaoId).orElse(null);

        if (member == null) {
            return "redirect:/members/login";
        }

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<WishResponse> wishPage = wishlistService.getWishesByMemberId(member.getId(), pageRequest);

        model.addAttribute("wishPage", wishPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "wishlist";
    }

    @GetMapping("/item-details/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long productId) {
        try {
            Product product = wishlistService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProductNumber(@PathVariable Long id, @RequestBody int productNumber) {
        try {
            wishlistService.updateProductNumber(id, productNumber);
            return ResponseEntity.ok("Successfully updated product quantity.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            wishlistService.deleteItem(id);
            return ResponseEntity.ok("Successfully deleted product.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found.");
        }
    }
}
