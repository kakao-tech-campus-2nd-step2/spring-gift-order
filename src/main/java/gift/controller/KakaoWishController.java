package gift.controller;

import gift.annotation.KakaoUser;
import gift.dto.*;

import gift.service.KakaoApiService;
import gift.service.OptionService;
import gift.service.WishService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kakao/wishes")
public class KakaoWishController {
    private final WishService wishService;
    private final OptionService optionService;
    private final KakaoApiService kakaoApiService;

    public KakaoWishController(WishService wishService, OptionService optionService, KakaoApiService kakaoApiService) {
        this.wishService = wishService;
        this.kakaoApiService = kakaoApiService;
        this.optionService = optionService;
    }

    @GetMapping
    public String getKakaoWishes(@KakaoUser KakaoUserDTO kakaoUserDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        WishPageResponseDTO wishOptions = wishService.getWishlist(kakaoUserDTO.user().getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "kakaoWishlist";
    }

    @GetMapping("/addWishOption")
    public String addWishOptionPage(@KakaoUser KakaoUserDTO kakaoUserDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        OptionsPageResponseDTO options = optionService.getAllOptions(pageable);
        model.addAttribute("options", options);

        return "addKakaoWishOption"; // addWishProduct.html로 이동
    }

    @PostMapping("/addWishOption")
    public ResponseEntity<String> addWishOption(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody WishRequestDTO wishRequestDTO, Model model) {
        wishService.addWishOption(kakaoUserDTO.user().getId(), wishRequestDTO);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/send/message")
    public ResponseEntity<Void> sendMessage(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        kakaoApiService.sendMessage(kakaoUserDTO.accessToken(), orderRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public String deleteWishOption(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody WishRequestDTO wishRequestDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        wishService.deleteWishOption(kakaoUserDTO.user().getId(), wishRequestDTO.optionId());

        WishPageResponseDTO wishOptions = wishService.getWishlist(kakaoUserDTO.user().getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "kakaoWishlist";
    }
}
