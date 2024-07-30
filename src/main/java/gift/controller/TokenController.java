package gift.controller;

import gift.service.BasicTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="basic-token",description = "Basic 토큰 API")
@RequestMapping("/token")
@RestController
public class TokenController {
    private final BasicTokenService basicTokenService;

    public TokenController(BasicTokenService basicTokenService) {
        this.basicTokenService = basicTokenService;
    }

    @GetMapping("/{userId}")
    public String makeTokenFrom(@RequestParam("userId") Long userId) {
        return basicTokenService.makeTokenFrom(userId.toString());
    }

}
