package gift.controller;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option API", description = "상품 옵션 관련 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "옵션 조회", description = "상품에 대한 모든 옵션을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = OptionResponse.class))})
    })
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.getOptions(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping
    @Operation(summary = "옵션 추가", description = "새로운 옵션을 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 추가 성공",
            content = {@Content(schema = @Schema(implementation = OptionResponse.class))}),
        @ApiResponse(responseCode = "400", description = "요청 데이터가 유효하지 않음 (옵션 이름 또는 해당 상품을 찾을 수 없음)")
    })
    public ResponseEntity<OptionResponse> addOption(@PathVariable Long productId,
        @Validated @RequestBody OptionRequest optionRequest) {
        OptionResponse optionResponse = optionService.addOption(productId, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(optionResponse);
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "옵션 삭제", description = "기존 옵션을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "요청 데이터가 유효하지 않음 (옵션 이름 또는 해당 옵션을 찾을 수 없음)"),
        @ApiResponse(responseCode = "404", description = "해당 옵션이 존재하지 않음")
    })
    public void deleteOption(@PathVariable Long optionId, @PathVariable Long productId) {
        optionService.deleteOption(optionId, productId);
    }

}
