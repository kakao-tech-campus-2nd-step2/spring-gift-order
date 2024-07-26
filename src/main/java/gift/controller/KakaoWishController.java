package gift.controller;

import gift.annotation.KakaoUser;
import gift.annotation.LoginUser;
import gift.dto.KakaoUserDTO;
import gift.dto.OrderRequestDTO;
import gift.dto.WishPageResponseDTO;
import gift.dto.WishRequestDTO;
import gift.model.User;
import gift.service.KakaoApiService;
import gift.service.WishService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kakao/wishes")
public class KakaoWishController {
    private final WishService wishService;
    private final KakaoApiService kakaoApiService;

    public KakaoWishController(WishService wishService, KakaoApiService kakaoApiService) {
        this.wishService = wishService;
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping
    public String getKakaoWishes(@KakaoUser KakaoUserDTO kakaoUserDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        WishPageResponseDTO wishOptions = wishService.getWishlist(kakaoUserDTO.user().getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "kakaoWishlist";
    }

    @PostMapping("/send/message")
    public ResponseEntity<Void> sendMessage(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        kakaoApiService.sendMessage(kakaoUserDTO.accessToken(), orderRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public String deleteWishProduct(@LoginUser User user, @RequestBody WishRequestDTO wishRequestDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        wishService.deleteWishProduct(user.getId(), wishRequestDTO.optionId());

        WishPageResponseDTO wishOptions = wishService.getWishlist(user.getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "kakaoWishlist";
    }
}
