package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Option;
import gift.entity.OptionDTO;
import gift.service.OptionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping()
    public ResponseEntity<List<Option>> getAllOptions() {
        return ResponseEntity.ok().body(optionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Option> getOptionById(@PathVariable Long id) {
        return ResponseEntity.ok().body(optionService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Option> createOption(@RequestBody @Valid OptionDTO optionDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        return ResponseEntity.ok().body(optionService.save(optionDTO, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable Long id, @RequestBody @Valid OptionDTO optionDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        return ResponseEntity.ok().body(optionService.update(id, optionDTO, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteOption(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        optionService.delete(id, email);
        return ResponseEntity.ok().body(new MessageResponseDTO("Option deleted successfully"));
    }
}
