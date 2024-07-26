package gift.controller;

import com.fasterxml.jackson.datatype.jdk8.OptionalSerializer;
import gift.domain.Menu;
import gift.domain.Option;
import gift.domain.OptionRequest;
import gift.repository.MenuRepository;
import gift.service.OptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/options")
public class OptionController {
    OptionService optionService;
    
    public OptionController(OptionService optionService,MenuRepository menuRepository){
        this.optionService = optionService;
    }
    
    @PostMapping("/save")
    public ResponseEntity<Option> save(
            @RequestBody OptionRequest optionRequest
    ){
        return ResponseEntity.ok().body(optionService.save(optionService));
    }

}
