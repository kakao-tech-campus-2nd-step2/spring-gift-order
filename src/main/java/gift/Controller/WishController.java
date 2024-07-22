package gift.Controller;

import gift.DTO.RequestWishDTO;
import gift.DTO.ResponseWishDTO;
import gift.Model.Entity.Member;
import gift.Service.WishService;
import gift.annotation.ValidUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class WishController {
    private final WishService wishService;


    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/wishes")
    public void addWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO) {
        wishService.addWish(member, requestWishDTO);
    }

    @GetMapping("/wishes")
    public ResponseEntity<Map<String, List<ResponseWishDTO>>> getWish(@ValidUser Member member) {
        List<ResponseWishDTO> list = wishService.getWish(member);
        Map<String, List<ResponseWishDTO>> response = new HashMap<>();
        response.put("wish", list);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/wishes")
    public ResponseEntity<Map<String, List<ResponseWishDTO>>> editWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO) {
        List<ResponseWishDTO> list = wishService.editWish(member, requestWishDTO);
        Map<String, List<ResponseWishDTO>> response = new HashMap<>();
        response.put("wish", list);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/wishes")
    public ResponseEntity<Map<String, List<ResponseWishDTO>>> deleteWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO) {
        List<ResponseWishDTO> list = wishService.deleteWish(member, requestWishDTO);
        Map<String, List<ResponseWishDTO>> response = new HashMap<>();
        response.put("wish", list);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}