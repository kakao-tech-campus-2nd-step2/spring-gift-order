package gift.Controller;

import gift.Annotation.LoginMemberResolver;
import gift.Model.MemberDto;
import gift.Model.OptionDto;
import gift.Model.OrderRequestDto;
import gift.Service.KakaoTalkService;
import gift.Service.OptionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OptionController {

    private final OptionService optionService;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OptionController(OptionService optionService, KakaoTalkService kakaoTalkService) {
        this.optionService = optionService;
        this.kakaoTalkService = kakaoTalkService;
    }

    @GetMapping("/api/products/options/{productId}")
    public ResponseEntity<List<OptionDto>> getAllOptionsByProductId(@PathVariable Long productId) {
        List<OptionDto> options = optionService.getAllOptionsByProductId(productId);
        if (options.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(options);
    }

    @GetMapping("/api/products/options/add/{productId}")
    public String addOption(Model model, @PathVariable("productId") long productId) {
        OptionDto optionDto = new OptionDto();
        optionDto.setProductId(productId);
        model.addAttribute("optionDto", optionDto);
        return "option_form";
    }

    @PostMapping("/api/products/options/add")
    public String addOption(@ModelAttribute OptionDto optionDto, Model model, @RequestParam("productId") long productId) {
        model.addAttribute("optionDto", optionDto);
        optionDto.setProductId(productId);
        optionService.addOption(optionDto);
        return "redirect:/api/products";
    }

    @GetMapping("/api/products/options/{productId}/{optionId}/update")
    public String updateOption(Model model, @PathVariable long productId, @PathVariable long optionId) {
        OptionDto optionDto = optionService.getOptionById(optionId);
        model.addAttribute("optionDto", optionDto);
        return "option_form";
    }

    @PostMapping("/api/products/options/{productId}/{optionId}/update")
    public ResponseEntity<?> updateOption(@PathVariable long productId, @PathVariable long optionId, @ModelAttribute OptionDto optionDto, Model model) {
        try {
            model.addAttribute("optionDto", optionDto);
            optionDto.setProductId(productId);
            optionDto.setId(optionId);
            optionService.updateOption(optionDto);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/products");
            return new ResponseEntity<>("Option updated successfully", headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating option");
        }
    }

    @DeleteMapping("/api/products/options/{optionId}/delete")
    public ResponseEntity<?> deleteOption(@PathVariable long optionId) {
        try {
            optionService.deleteOption(optionId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/products");
            return new ResponseEntity<>("Option updated successfully", headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting option");
        }
    }

    @PostMapping("/option/purchase")
    public ResponseEntity<String> purchaseWishlist(@LoginMemberResolver MemberDto memberDto, @RequestBody List<OrderRequestDto> orderRequestDtoList, HttpServletRequest request) {
        for (OrderRequestDto orderRequestDto : orderRequestDtoList) {
            orderRequestDto.setMemberId(memberDto.getId());
        }
        optionService.subtractOption(orderRequestDtoList);

        //카카오 토큰 가져오기
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        //토큰으로 메시지 보내기
        if (token != null) { //
            try {
                kakaoTalkService.sendMessageToMe(token, orderRequestDtoList);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error sending message");
            }
        }

        return ResponseEntity.ok("Purchase successful");
    }
}
