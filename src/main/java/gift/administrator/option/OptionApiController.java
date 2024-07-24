package gift.administrator.option;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OptionApiController {

    private final OptionService optionService;

    public OptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/products/{productId}/options")
    public ResponseEntity<List<OptionDTO>> getAllOptionsByProductId(
        @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(optionService.getAllOptionsByProductId(productId));
    }

    @GetMapping("/options")
    public ResponseEntity<List<OptionDTO>> getAllOptions() {
        return ResponseEntity.ok(optionService.getAllOptions());
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteOptionByOptionId(
        @PathVariable("optionId") long optionId) {
        if (optionService.countAllOptionsByProductIdFromOptionId(optionId) == 1) {
            throw new IllegalArgumentException("상품에는 적어도 하나의 옵션이 있어야 합니다.");
        }
        optionService.deleteOptionByOptionId(optionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<OptionDTO> updateOptionByProductIdAndOptionId(
        @PathVariable("productId") long productId, @PathVariable("optionId") long optionId,
        @Valid @RequestBody OptionDTO optionDTO) {
        optionService.existsByNameSameProductIdNotOptionId(optionDTO.getName(), productId, optionId);
        return ResponseEntity.ok(optionService.updateOption(optionId, optionDTO));
    }
}
