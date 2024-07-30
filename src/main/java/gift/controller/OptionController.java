package gift.controller;


import gift.classes.RequestState.OptionRequestStateDTO;
import gift.classes.RequestState.RequestStateDTO;
import gift.classes.RequestState.RequestStatus;
import gift.dto.OptionDto;
import gift.dto.RequestOptionDto;
import gift.services.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/options")
@Tag(name = "OptionController", description = "Option API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    //    제품 아이디로 Option 조회
    @GetMapping("/{productId}/options")
    @Operation(summary = "제품 옵션 조회", description = "제품 ID로 해당 제품의 옵션을 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 조회 성공"),
        @ApiResponse(responseCode = "400", description = "옵션 조회 실패(잘못된 요청)")
    })
    public ResponseEntity<OptionRequestStateDTO> getOptionsByProductId(
        @PathVariable Long productId) {
        List<OptionDto> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok().body(new OptionRequestStateDTO(
            RequestStatus.success,
            null,
            options
        ));
    }

////    Option 추가
//    @PostMapping("/{productId}/options")
//    public ResponseEntity<RequestStateDTO> addOption(@PathVariable Long productId, @Valid @RequestBody
//    RequestOptionDto requestOptionDto) {
//        optionService.addOption(productId, requestOptionDto);
//        return ResponseEntity.ok().body(new RequestStateDTO(
//            RequestStatus.success,
//            null
//        ));
//    }

    //    Option 수정
    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "옵션 수정", description = "옵션 ID로 옵션 정보를 수정하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 수정 성공"),
        @ApiResponse(responseCode = "400", description = "옵션 수정 실패"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<RequestStateDTO> updateOption(@PathVariable Long optionId,
        @Valid @RequestBody
        RequestOptionDto requestOptionDto) {
        optionService.updateOption(optionId, requestOptionDto);
        return ResponseEntity.ok().body(new RequestStateDTO(
            RequestStatus.success,
            null
        ));
    }

    //    Option 삭제
    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "옵션 삭제", description = "옵션 ID로 옵션을 삭제하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "옵션 삭제 실패(잘못된 요청)"),
        @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public void deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
    }

}
