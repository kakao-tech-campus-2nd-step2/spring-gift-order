package gift.controller;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option Management", description = "상품 옵션 관리 API")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "옵션 목록 조회", description = "상품의 옵션 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.findByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.")
    @PostMapping
    public ResponseEntity<OptionResponse> addOption(@PathVariable Long productId, @RequestBody OptionRequest optionRequest) {
        optionRequest = new OptionRequest(optionRequest.name(), optionRequest.quantity(), productId);
        OptionResponse savedOption = optionService.save(optionRequest);
        return ResponseEntity.ok(savedOption);
    }
}
