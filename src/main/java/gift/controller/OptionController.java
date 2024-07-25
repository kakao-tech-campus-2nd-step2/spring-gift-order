package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OptionDTO;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.OptionFacadeService;
import gift.service.OptionService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/option")
public class OptionController {

    private final OptionFacadeService optionService;

    public OptionController(OptionFacadeService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<String> addOption(@RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(optionDTO.getProductId());
        Option option = optionDTO.toEntity(product);
        optionService.addOption(option);

        return new ResponseEntity<>("Option 추가 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateOption(@PathVariable("id") Long id,
        @RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(optionDTO.getProductId());
        Option option = optionDTO.toEntity(product);

        optionService.updateOption(option, id);
        return new ResponseEntity<>("Option 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOption(@PathVariable("id") Long id) {
        optionService.deleteOption(id);
        return new ResponseEntity<>("Option 삭제 완료", HttpStatus.NO_CONTENT);
    }


    //상품 주문
    @PostMapping("/orders")
    public ResponseEntity<String> orderOption(@RequestBody OrderRequestDTO orderRequestDTO,
        @LoginUser String email) {
        OrderResponseDTO response = optionService.orderOption(orderRequestDTO, email);

        return new ResponseEntity<>("옵션 주문 및 메시지 보내기 완료", HttpStatus.OK);
    }

}
