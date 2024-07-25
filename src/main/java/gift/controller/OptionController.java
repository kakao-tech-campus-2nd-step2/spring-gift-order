package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.option.OptionQuantityDTO;
import gift.dto.option.OrderResponseDTO;
import gift.dto.option.SaveOptionDTO;
import gift.dto.option.UpdateOptionDTO;
import gift.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OptionController {
    private final OptionService optionService;

    @PostMapping("/api/option")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOption(@RequestBody SaveOptionDTO saveOptionDTO) {
        optionService.saveOption(saveOptionDTO);
    }

    @DeleteMapping("/api/option/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteOption(@PathVariable int id) {
        optionService.deleteOption(id);
    }

    @PutMapping("/api/option")
    @ResponseStatus(HttpStatus.OK)
    public void updateOption(@RequestBody UpdateOptionDTO updateOptionDTO) {
        optionService.updateOption(updateOptionDTO);
    }

    @PostMapping("/api/option/refill")
    @ResponseStatus(HttpStatus.OK)
    public void refill(@RequestBody OptionQuantityDTO optionQuantityDTO) {
        optionService.refillQuantity(optionQuantityDTO);
    }

    @PostMapping("/api/option/order")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public OrderResponseDTO order(@RequestHeader("Authorization") String token, @RequestBody OptionQuantityDTO optionQuantityDTO) {
        return optionService.order(optionQuantityDTO, token);
    }
}
