package gift.controller;

import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.option.OptionRequestDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }


    @GetMapping
    public ResponseEntity<?> getOneProductIdAllOptions(@PathVariable Long productId) {
        return new ResponseEntity<>(optionService.getOneProductIdAllOptions(productId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addOption(@PathVariable Long productId, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        optionService.addOption(productId, optionRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.CREATED);
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<ResponseDTO> updateOption(@PathVariable Long productId, @PathVariable Long optionId,
            @RequestBody OptionRequestDTO optionRequestDTO) {
        optionService.updateOption(productId, optionId, optionRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<ResponseDTO> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }
}
