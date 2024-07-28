package gift.controller;

import gift.domain.model.dto.OptionAddRequestDto;
import gift.domain.model.dto.OptionResponseDto;
import gift.domain.model.dto.OptionUpdateRequestDto;
import gift.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Validated
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> getAllOptionsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(optionService.getAllOptionsByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<OptionResponseDto> addOption(
        @PathVariable Long productId,
        @Valid @RequestBody OptionAddRequestDto optionAddRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(optionService.addOption(productId,
            optionAddRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionResponseDto> updateOption(
        @PathVariable Long id,
        @Valid @RequestBody OptionUpdateRequestDto optionUpdateRequestDto) {
        return ResponseEntity.ok(optionService.updateOption(id, optionUpdateRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }
}