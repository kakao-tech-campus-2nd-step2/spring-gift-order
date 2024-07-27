package gift.controller.rest;

import gift.entity.Option;
import gift.entity.OptionDTO;
import gift.service.OptionService;
import gift.util.ResponseUtility;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;
    private final ResponseUtility responseUtility;

    @Autowired
    public OptionController(OptionService optionService, ResponseUtility responseUtility) {
        this.optionService = optionService;
        this.responseUtility = responseUtility;
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
    public ResponseEntity<Map<String, String>> deleteOption(@PathVariable Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        optionService.delete(id, email);
        Map<String, String> response = responseUtility.makeResponse("deleted successfully");
        return ResponseEntity.ok().body(response);
    }
}
